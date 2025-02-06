package com.shineidle.tripf.domain.feed.service;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.FeedErrorCode;
import com.shineidle.tripf.global.common.exception.type.LockErrorCode;
import com.shineidle.tripf.global.common.message.constants.NotificationMessage;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.global.common.message.type.PostMessage;
import com.shineidle.tripf.global.common.util.provider.JwtProvider;
import com.shineidle.tripf.global.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.domain.feed.dto.*;
import com.shineidle.tripf.domain.feed.entity.Activity;
import com.shineidle.tripf.domain.feed.entity.Days;
import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.feed.repository.ActivityRepository;
import com.shineidle.tripf.domain.feed.repository.DaysRepository;
import com.shineidle.tripf.domain.feed.repository.FeedRepository;
import com.shineidle.tripf.domain.follow.dto.FollowResponseDto;
import com.shineidle.tripf.domain.follow.service.FollowService;
import com.shineidle.tripf.domain.geo.service.GeoService;
import com.shineidle.tripf.domain.notification.service.NotificationService;
import com.shineidle.tripf.domain.notification.type.NotifyType;
import com.shineidle.tripf.domain.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final DaysRepository daysRepository;
    private final ActivityRepository activityRepository;
    private final GeoService geoService;
    private final FollowService followService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final RedissonClient redissonClient;
    private final JwtProvider jwtProvider;
    private final RedisFeedService redisFeedService;
    private final RedisTemplate<String, Object> feedRedisTemplate;

    /**
     * 피드 생성
     * Redis 분산락 사용
     *
     * @param token          토큰
     * @param feedRequestDto {@link FeedRequestDto} 피드 요청 Dto
     * @return {@link FeedResponseDto} 피드 응답 Dto
     */
    @Override
    @Transactional
    public FeedResponseDto createFeed(FeedRequestDto feedRequestDto, String token) {
        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 객체 설정

        User userId = UserAuthorizationUtil.getLoginUser();

        String lockKey = "createFeed:lock:user:" + userId.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {

                String country = geoService.getCountryByCity(feedRequestDto.getCity());

                Feed feed = new Feed(
                        userId,
                        country,
                        feedRequestDto.getCity(),
                        feedRequestDto.getStartedAt(),
                        feedRequestDto.getEndedAt(),
                        feedRequestDto.getTitle(),
                        feedRequestDto.getContent(),
                        feedRequestDto.getCost(),
                        feedRequestDto.getTag()
                );
                Feed savedFeed = feedRepository.save(feed);

                List<DaysResponseDto> daysResponseDtos = new ArrayList<>();

                if (feedRequestDto.getDays() != null) {
                    feedRequestDto.getDays().forEach(daysRequestDto -> {
                        Days days = new Days(savedFeed, daysRequestDto.getDate());

                        LocalDate date = daysRequestDto.getDate();
                        if (days.getDate().isBefore(feed.getStartedAt().toLocalDate()) || days.getDate().isAfter(feed.getEndedAt().toLocalDate())) {
                            throw new GlobalException(FeedErrorCode.DATE_INVALID);
                        }

                        if (daysRepository.existsByFeedAndDate(feed, date)) {
                            throw new GlobalException(FeedErrorCode.DATE_DUPLICATE);
                        }

                        Days savedDays = daysRepository.save(days);
                        List<ActivityResponseDto> activityDtos = new ArrayList<>();

                        if (daysRequestDto.getActivity() != null) {
                            daysRequestDto.getActivity().forEach(activityRequestDto -> {
                                Activity activity = new Activity(
                                        savedDays,
                                        activityRequestDto.getTitle(),
                                        activityRequestDto.getStar(),
                                        activityRequestDto.getMemo(),
                                        activityRequestDto.getCity(),
                                        activityRequestDto.getLatitude(),
                                        activityRequestDto.getLongitude()
                                );
                                activityRepository.save(activity);

                                List<PhotoResponseDto> photoDtos = activity.getActivityPhotos().stream()
                                        .map(activityPhoto -> PhotoResponseDto.toDto(activityPhoto.getPhoto()))
                                        .toList();

                                String representativePhotoUrl = activity.getRepresentativePhotoUrl();

                                activityDtos.add(ActivityResponseDto.toDto(activity, photoDtos, representativePhotoUrl));
                            });
                        }

                        daysResponseDtos.add(new DaysResponseDto(savedDays.getId(), savedDays.getDate(), activityDtos));
                    });
                }

                followerNewPostNotification(userId, feed.getId());

                FeedResponseDto newFeed = FeedResponseDto.toDto(savedFeed, daysResponseDtos);

                redisFeedService.updateCache(savedFeed.getId(), newFeed);

                redisFeedService.deleteCache("publicHomeCache");
                redisFeedService.deleteCache("region:" + country);

                return newFeed;
            } else {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 피드 생성
     * Redis 분산락 사용
     *
     * @param feedRequestDto {@link FeedRequestDto} 피드 요청 Dto
     * @return {@link FeedResponseDto} 피드 응답 Dto
     */
    @Override
    @Transactional
    public FeedResponseDto createFeed(FeedRequestDto feedRequestDto) {
        User userId = UserAuthorizationUtil.getLoginUser();

        String lockKey = "createFeed:lock:user:" + userId.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {

                String country = geoService.getCountryByCity(feedRequestDto.getCity());

                Feed feed = new Feed(
                        userId,
                        country,
                        feedRequestDto.getCity(),
                        feedRequestDto.getStartedAt(),
                        feedRequestDto.getEndedAt(),
                        feedRequestDto.getTitle(),
                        feedRequestDto.getContent(),
                        feedRequestDto.getCost(),
                        feedRequestDto.getTag()
                );
                Feed savedFeed = feedRepository.save(feed);

                List<DaysResponseDto> daysResponseDtos = new ArrayList<>();

                if (feedRequestDto.getDays() != null) {
                    feedRequestDto.getDays().forEach(daysRequestDto -> {
                        Days days = new Days(savedFeed, daysRequestDto.getDate());

                        LocalDate date = daysRequestDto.getDate();
                        if (days.getDate().isBefore(feed.getStartedAt().toLocalDate()) || days.getDate().isAfter(feed.getEndedAt().toLocalDate())) {
                            throw new GlobalException(FeedErrorCode.DATE_INVALID);
                        }

                        if (daysRepository.existsByFeedAndDate(feed, date)) {
                            throw new GlobalException(FeedErrorCode.DATE_DUPLICATE);
                        }

                        Days savedDays = daysRepository.save(days);
                        List<ActivityResponseDto> activityDtos = new ArrayList<>();

                        if (daysRequestDto.getActivity() != null) {
                            daysRequestDto.getActivity().forEach(activityRequestDto -> {
                                Activity activity = new Activity(
                                        savedDays,
                                        activityRequestDto.getTitle(),
                                        activityRequestDto.getStar(),
                                        activityRequestDto.getMemo(),
                                        activityRequestDto.getCity(),
                                        activityRequestDto.getLatitude(),
                                        activityRequestDto.getLongitude()
                                );
                                activityRepository.save(activity);

                                List<PhotoResponseDto> photoDtos = activity.getActivityPhotos().stream()
                                        .map(activityPhoto -> PhotoResponseDto.toDto(activityPhoto.getPhoto()))
                                        .toList();

                                String representativePhotoUrl = activity.getRepresentativePhotoUrl();

                                activityDtos.add(ActivityResponseDto.toDto(activity, photoDtos, representativePhotoUrl));
                            });
                        }

                        daysResponseDtos.add(new DaysResponseDto(savedDays.getId(), savedDays.getDate(), activityDtos));
                    });
                }

                followerNewPostNotification(userId, feed.getId());

                FeedResponseDto newFeed = FeedResponseDto.toDto(savedFeed, daysResponseDtos);

                redisFeedService.updateCache(savedFeed.getId(), newFeed);

                redisFeedService.deleteCache("publicHomeCache");
                redisFeedService.deleteCache("region:" + country);

                return newFeed;
            } else {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 피드 수정
     *
     * @param feedId         피드 식별자
     * @param feedRequestDto {@link FeedRequestDto} 피드 요청 DTO
     * @return {@link FeedResponseDto} 피드 응답 DTO
     */
    @Override
    @Transactional
    public FeedResponseDto updateFeed(Long feedId, FeedRequestDto feedRequestDto) {
        Feed feed = validateFeedOwnership(feedId);

        String country = geoService.getCountryByCity(feedRequestDto.getCity());

        feed.update(
                country,
                feedRequestDto.getCity(),
                feedRequestDto.getStartedAt(),
                feedRequestDto.getEndedAt(),
                feedRequestDto.getTitle(),
                feedRequestDto.getContent(),
                feedRequestDto.getCost(),
                feedRequestDto.getTag()
        );
        feedRepository.save(feed);

        FeedResponseDto updatedFeed = findFeed(feedId);

        redisFeedService.updateCache(feedId, updatedFeed);
        redisFeedService.deleteCache("homeCache");
        redisFeedService.deleteCache("region:" + country);
        return findFeed(feedId);
    }

    /**
     * 피드 상세조회
     *
     * @param feedId 피드 식별자
     * @return {@link FeedResponseDto} 피드 응답 Dto
     */
    @Override
    public FeedResponseDto findFeed(Long feedId) {
        FeedResponseDto cachedFeed = redisFeedService.getFeed(feedId);
        if (cachedFeed != null) {
            log.info("Redis 캐시에서 FeedResponseDto 조회 성공: {}", feedId);
            return cachedFeed;
        }

        log.info("Redis 캐시에 없음, DB 조회 수행: {}", feedId);
        Feed feed = checkFeed(feedId);

        List<Days> daysList = daysRepository.findAllByFeedId(feedId);

        List<Long> daysIds = daysList.stream()
                .map(Days::getId)
                .toList();

        List<Activity> activities = activityRepository.findAllWithPhotosByDaysIds(daysIds);

        Map<Long, List<ActivityResponseDto>> activityMap = activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> activity.getDays().getId(),
                        Collectors.mapping(activity -> {
                            List<PhotoResponseDto> photoDtos = activity.getActivityPhotos().stream()
                                    .map(activityPhoto -> PhotoResponseDto.toDto(activityPhoto.getPhoto()))
                                    .toList();

                            return ActivityResponseDto.toDto(activity, photoDtos, activity.getRepresentativePhotoUrl());
                        }, Collectors.toList())
                ));

        List<DaysResponseDto> daysResponseDtos = daysList.stream()
                .map(days -> new DaysResponseDto(days.getId(), days.getDate(), activityMap.getOrDefault(days.getId(), List.of())))
                .toList();

        FeedResponseDto feedResponseDto = FeedResponseDto.toDto(feed, daysResponseDtos);

        redisFeedService.saveFeed(feedId, feedResponseDto);
        log.info("FeedResponseDto를 Redis에 캐싱 완료: {}", feedId);

        return feedResponseDto;
    }

    /**
     * 피드 삭제
     *
     * @param feedId 피드 식별자
     * @return {@link PostMessageResponseDto} 피드 삭제 문구
     */
    @Override
    public PostMessageResponseDto deleteFeed(Long feedId) {
        Feed feed = validateFeedOwnership(feedId);
        feed.markAsDeleted();
        feedRepository.save(feed);

        feedRedisTemplate.delete("feed:" + feedId);

        return new PostMessageResponseDto(PostMessage.PEED_DELETED);
    }

    /**
     * 일정 삭제
     *
     * @param feedId 피드 식별자
     * @param daysId 일정 식별자
     * @return {@link PostMessageResponseDto} 일정 삭제 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto deleteDays(Long feedId, Long daysId) {
        Feed feed = validateFeedOwnership(feedId);

        daysRepository.deleteById(daysId);

        FeedResponseDto updatedFeed = findFeed(feedId);
        redisFeedService.updateCache(feedId, updatedFeed);

        redisFeedService.deleteCache("homeCache");
        redisFeedService.deleteCache("region:" + checkFeed(feedId).getCountry());

        return new PostMessageResponseDto(PostMessage.DAYS_DELETED);
    }


    /**
     * 활동 삭제
     *
     * @param feedId     피드 식별자
     * @param daysId     일정 식별자
     * @param activityId 활동 식별자
     * @return {@link PostMessageResponseDto} 활동 삭제 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto deleteActivity(Long feedId, Long daysId, Long activityId) {
        Feed feed = validateFeedOwnership(feedId);

        activityRepository.deleteById(activityId);

        FeedResponseDto updatedFeed = findFeed(feedId);
        redisFeedService.updateCache(feedId, updatedFeed);

        redisFeedService.deleteCache("homeCache");
        redisFeedService.deleteCache("region:" + checkFeed(feedId).getCountry());

        return new PostMessageResponseDto(PostMessage.ACTIVITY_DELETED);
    }

    /**
     * 일정 및 활동 추가
     * 일정 추가 시 활동도 같이 추가
     *
     * @param feedId         피드 식별자
     * @param daysRequestDto {@link DaysRequestDto} 일정 요청 Dto
     * @return {@link FeedResponseDto} 일정 응답 Dto
     */
    @Override
    @Transactional
    public FeedResponseDto createDay(Long feedId, DaysRequestDto daysRequestDto) {
        Feed feed = validateFeedOwnership(feedId);
        Days days = new Days(feed, daysRequestDto.getDate());

        LocalDate date = daysRequestDto.getDate();
        if (days.getDate().isBefore(feed.getStartedAt().toLocalDate()) || days.getDate().isAfter(feed.getEndedAt().toLocalDate())) {
            throw new GlobalException(FeedErrorCode.DATE_INVALID);
        }

        if (daysRepository.existsByFeedAndDate(feed, date)) {
            throw new GlobalException(FeedErrorCode.DATE_DUPLICATE);
        }

        Days savedDays = daysRepository.save(days);

        if (daysRequestDto.getActivity() != null) {
            daysRequestDto.getActivity()
                    .forEach(activityRequestDto -> {
                        Activity activity = new Activity(
                                savedDays,
                                activityRequestDto.getTitle(),
                                activityRequestDto.getStar(),
                                activityRequestDto.getMemo(),
                                activityRequestDto.getCity(),
                                activityRequestDto.getLatitude(),
                                activityRequestDto.getLongitude()
                        );
                        activityRepository.save(activity);
                    });
        }

        FeedResponseDto updatedFeed = findFeed(feedId);
        redisFeedService.updateCache(feedId, updatedFeed);

        redisFeedService.deleteCache("homeCache");
        redisFeedService.deleteCache("region:" + checkFeed(feedId).getCountry());

        return updatedFeed;
    }

    /**
     * 활동 추가
     *
     * @param feedId             피드 식별자
     * @param daysId             일정 식별자
     * @param activityRequestDto {@link ActivityRequestDto} 활동 요청 Dto
     * @return {@link FeedResponseDto} 활동 응답 Dto
     */
    @Override
    @Transactional
    public FeedResponseDto createActivity(Long feedId, Long daysId, ActivityRequestDto activityRequestDto) {
        Feed feed = validateFeedOwnership(feedId);
        Days days = checkDays(feedId, daysId);

        Activity activity = new Activity(
                days,
                activityRequestDto.getTitle(),
                activityRequestDto.getStar(),
                activityRequestDto.getMemo(),
                activityRequestDto.getCity(),
                activityRequestDto.getLatitude(),
                activityRequestDto.getLongitude()
        );
        activityRepository.save(activity);

        FeedResponseDto updatedFeed = findFeed(feedId);
        redisFeedService.updateCache(feedId, updatedFeed);

        redisFeedService.deleteCache("homeCache");
        redisFeedService.deleteCache("region:" + checkFeed(feedId).getCountry());

        return updatedFeed;
    }

    /**
     * 활동 수정
     *
     * @param feedId             피드 식별자
     * @param daysId             일정 식별자
     * @param activityId         활동 식별자
     * @param activityRequestDto {@link ActivityRequestDto} 활동 요청 Dto
     * @return {@link FeedResponseDto} 활동 응답 Dto
     */
    @Override
    @Transactional
    public FeedResponseDto updateActivity(Long feedId, Long daysId, Long activityId, ActivityRequestDto activityRequestDto) {
        Feed feed = validateFeedOwnership(feedId);
        Activity activity = checkActivity(feedId, daysId, activityId);

        activity.update(
                activityRequestDto.getTitle(),
                activityRequestDto.getStar(),
                activityRequestDto.getMemo(),
                activityRequestDto.getCity(),
                activityRequestDto.getLatitude(),
                activityRequestDto.getLongitude()
        );
        activityRepository.save(activity);

        FeedResponseDto updatedFeed = findFeed(feedId);
        redisFeedService.updateCache(feedId, updatedFeed);

        redisFeedService.deleteCache("homeCache");
        redisFeedService.deleteCache("region:" + checkFeed(feedId).getCountry());

        return updatedFeed;
    }

    /**
     * 국가별 피드 조회
     *
     * @param country 국가명
     * @return {@link RegionResponseDto} 지역 응답 Dto
     */
    @Override
    public List<RegionResponseDto> findRegion(String country) {
        String cacheKey = "region:" + country;

        List<RegionResponseDto> cachedRegionFeeds = redisFeedService.getRegionCache(cacheKey);
        if (cachedRegionFeeds != null) {
            return cachedRegionFeeds;
        }

        List<Feed> feeds = feedRepository.findByCountryAndDeletedAtIsNull(country);
        List<RegionResponseDto> regionFeeds = feeds.stream()
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return RegionResponseDto.toDto(feed, representativePhotoUrl);
                })
                .toList();

        redisFeedService.saveRegionCache(cacheKey, regionFeeds);

        return regionFeeds;
    }

    /**
     * 로그인한 사용자에 대한 홈페이지
     *
     * @return HomeResponseDto
     */
    @Override
    public HomeResponseDto findHomeData() {
        String cacheKey = "homeCache";

        HomeResponseDto cachedHomeData = redisFeedService.getHomeCache(cacheKey);
        if (cachedHomeData != null) {
            return cachedHomeData;
        }

        List<RegionResponseDto> korea = feedRepository.findByCountry("대한민국")
                .stream()
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return RegionResponseDto.toDto(feed, representativePhotoUrl);
                })
                .toList();

        List<RegionResponseDto> global = feedRepository.findByCountryNot("대한민국")
                .stream()
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return RegionResponseDto.toDto(feed, representativePhotoUrl);
                })
                .toList();

        List<FollowResponseDto> followers = followService.findFollowers();
        List<FollowResponseDto> followings = followService.findFollowings();

        HomeResponseDto homeData = new HomeResponseDto(korea, global, followers, followings);

        redisFeedService.saveHomeCache(cacheKey, homeData);

        return homeData;
    }

    /**
     * 비로그인자에 대한 홈페이지
     *
     * @return HomeResponseDto
     */
    @Override
    public HomeResponseDto findPublicHomeData() {
        String cacheKey = "publicHomeCache";

        HomeResponseDto cachedHomeData = redisFeedService.getPublicHomeCache(cacheKey);
        if (cachedHomeData != null) {
            return cachedHomeData;
        }

        List<RegionResponseDto> korea = feedRepository.findByCountry("대한민국")
                .stream()
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return RegionResponseDto.toDto(feed, representativePhotoUrl);
                })
                .toList();

        List<RegionResponseDto> global = feedRepository.findByCountryNot("대한민국")
                .stream()
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return RegionResponseDto.toDto(feed, representativePhotoUrl);
                })
                .toList();

        HomeResponseDto homeData = new HomeResponseDto(korea, global, List.of(), List.of());

        redisFeedService.savePublicHomeCache(cacheKey, homeData);

        return homeData;
    }

    /**
     * 본인 소유 피드 조회
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<MyFeedResponseDto> findMyFeeds(Pageable pageable) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        String cacheKey = "myFeeds:" + loginUser.getId() + ":" + pageable.getPageNumber() + ":" + pageable.getPageSize();

        Page<MyFeedResponseDto> cachedMyFeeds = redisFeedService.getMyFeedsCache(cacheKey, pageable);
        if (cachedMyFeeds != null) {
            return cachedMyFeeds;
        }

        Page<MyFeedResponseDto> myFeeds = feedRepository.findByUserIdAndDeletedAtIsNull(loginUser.getId(), pageable)
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return MyFeedResponseDto.toDto(feed, representativePhotoUrl);
                });

        redisFeedService.saveMyFeedsCache(cacheKey, myFeeds);

        return myFeeds;
    }

    /**
     * 피드 Id로 피드 확인
     *
     * @param feedId 피드 식별자
     * @return {@link Feed}
     */
    public Feed checkFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.FEED_NOT_FOUND));

        if (feed.isDeleted()) {
            throw new GlobalException(FeedErrorCode.FEED_ALREADY_DELETED);
        }

        return feed;
    }

    /**
     * 피드 및 일정 Id로 일정 확인
     *
     * @param feedId 피드 식별자
     * @param daysId 일정 식별자
     * @return {@link Days}
     */
    public Days checkDays(Long feedId, Long daysId) {
        return daysRepository.findByIdWithFeed(feedId, daysId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.DAYS_NOT_FOUND));
    }

    /**
     * 피드 및 일정 및 피드 Id로 활동 확인
     *
     * @param feedId     피드 식별자
     * @param daysId     일정 식별자
     * @param activityId 활동 식별자
     * @return {@link Activity}
     */
    public Activity checkActivity(Long feedId, Long daysId, Long activityId) {
        return activityRepository.findByIdWithFeedAndDays(feedId, daysId, activityId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.ACTIVITY_NOT_FOUND));
    }

    /**
     * 피드 검증 메서드 (존재 여부 확인 + 삭제 여부 확인 + 작성자 검증)
     *
     * @param feedId 피드 식별자
     * @return {@link Feed}
     */
    public Feed validateFeedOwnership(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.FEED_NOT_FOUND));

        if (feed.isDeleted()) {
            throw new GlobalException(FeedErrorCode.FEED_ALREADY_DELETED);
        }

        Long loginUserId = UserAuthorizationUtil.getLoginUserId();
        if (!feed.getUser().getId().equals(loginUserId)) {
            throw new GlobalException(FeedErrorCode.FEED_CANNOT_ACCESS);
        }
        return feed;
    }

    /**
     * 팔로우 한사람이 새로운 게시글을 작성할 경우 알림
     *
     * @param actor  알림 발생자
     * @param feedId 알림 발생 피드
     */
    private void followerNewPostNotification(User actor, Long feedId) {
        followService.findFollowers().stream()
                .map(follower -> userService.getUserById(follower.getUserId()))
                .forEach(targetUser -> {
                    String context = String.format(NotificationMessage.FOLLOW_FEED_NOTIFICATION, actor.getName());
                    notificationService.createNotification(targetUser, actor, NotifyType.NEW_FEED, context, feedId);
                });
    }

    public String getRepresentativePhotoUrl(Long feedId) {
        List<Days> daysList = daysRepository.findByFeedId(feedId);

        return daysList.stream()
                .flatMap(day -> activityRepository.findByDays(day).stream())
                .flatMap(activity -> activity.getActivityPhotos().stream())
                .map(activityPhoto -> activityPhoto.getPhoto().getUrl())
                .findFirst()
                .orElse(null); // 대표 사진이 없으면 null 반환
    }
}
