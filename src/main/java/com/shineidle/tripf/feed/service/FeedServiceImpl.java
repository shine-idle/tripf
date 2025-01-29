package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FeedErrorCode;
import com.shineidle.tripf.common.exception.type.LockErrorCode;
import com.shineidle.tripf.common.message.constants.NotificationMessage;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.feed.dto.*;
import com.shineidle.tripf.feed.entity.Activity;
import com.shineidle.tripf.feed.entity.Days;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.feed.repository.ActivityRepository;
import com.shineidle.tripf.feed.repository.DaysRepository;
import com.shineidle.tripf.feed.repository.FeedRepository;
import com.shineidle.tripf.follow.dto.FollowResponseDto;
import com.shineidle.tripf.follow.service.FollowService;
import com.shineidle.tripf.geo.service.GeoService;
import com.shineidle.tripf.notification.service.NotificationService;
import com.shineidle.tripf.notification.type.NotifyType;
import com.shineidle.tripf.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                            });
                        }
                    });
                }

                followerNewPostNotification(userId, feed.getId());

                return findFeed(savedFeed.getId());
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
        checkUser(feedId);
        Feed feed = checkFeed(feedId);

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
        Feed feed = checkFeed(feedId);

        List<Days> daysList = daysRepository.findByFeed(feed);

        List<DaysResponseDto> daysResponseDto = daysList.stream()
                .map(days -> {
                    List<ActivityResponseDto> activityResponseDto = activityRepository.findByDays(days)
                            .stream()
                            .map(activity -> {
                                List<PhotoResponseDto> photoDtos = activity.getActivityPhotos().stream()
                                        .map(activityPhoto -> PhotoResponseDto.toDto(activityPhoto.getPhoto()))
                                        .toList();

                                String representativePhotoUrl = activity.getRepresentativePhotoUrl();

                                return ActivityResponseDto.toDto(activity, photoDtos, representativePhotoUrl);
                            }).toList();

                    return new DaysResponseDto(
                            days.getId(),
                            days.getDate(),
                            activityResponseDto
                    );
                }).toList();

        return FeedResponseDto.toDto(feed, daysResponseDto);
    }

    /**
     * 피드 삭제
     *
     * @param feedId 피드 식별자
     * @return {@link PostMessageResponseDto} 피드 삭제 문구
     */
    @Override
    public PostMessageResponseDto deleteFeed(Long feedId) {
        checkUser(feedId);
        Feed feed = checkFeed(feedId);
        feed.markAsDeleted();
        feedRepository.save(feed);
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
        checkUser(feedId);
        checkDays(feedId, daysId);
        daysRepository.deleteById(daysId);
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
        checkUser(feedId);
        checkActivity(feedId, daysId, activityId);
        activityRepository.deleteById(activityId);
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
        checkUser(feedId);
        Feed feed = checkFeed(feedId);
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
        return findFeed(feedId);
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
        checkUser(feedId);
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

        return findFeed(feedId);
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
        checkUser(feedId);
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

        return findFeed(feedId);
    }

    /**
     * 국가별 피드 조회
     *
     * @param country 국가명
     * @return {@link RegionResponseDto} 지역 응답 Dto
     */
    @Override
//    @Cacheable(value = "regionCache", key = "'country:' + #country")
    public List<RegionResponseDto> findRegion(String country) {

        List<Feed> feeds = feedRepository.findByCountryAndDeletedAtIsNull(country);

        return feeds.stream()
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return RegionResponseDto.toDto(feed, representativePhotoUrl);
                })
                .collect(Collectors.toList());
    }

    @Override
    public HomeResponseDto findHomeData() {
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

        return new HomeResponseDto(korea, global, followers, followings);
    }

    @Override
    public Page<MyFeedResponseDto> findMyFeeds(Pageable pageable) {
        // 로그인한 사용자 가져오기
        User loginUser = UserAuthorizationUtil.getLoginUser();

        // 본인 피드 조회
        return feedRepository.findByUserIdAndDeletedAtIsNull(loginUser.getId(), pageable)
                .map(feed -> {
                    String representativePhotoUrl = getRepresentativePhotoUrl(feed.getId());
                    return MyFeedResponseDto.toDto(feed, representativePhotoUrl);
                });
    }

    /**
     * 홈페이지
     * @return
     */
    @Override
    public HomeResponseDto findPublicHomeData() {

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

        return new HomeResponseDto(korea, global, List.of(), List.of());
    }


    /**
     * repository Service method
     */

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
     * 작성자 검증 method
     * 로그인한 유저와 작성자가 다를 경우 exception
     *
     * @param feedId 피드 식별자
     */
    public void checkUser(Long feedId) {
        Feed checkFeed = checkFeed(feedId);
        Long checkUser = UserAuthorizationUtil.getLoginUserId();

        if (!checkFeed.getUser().getId().equals(checkUser)) {
            throw new GlobalException(FeedErrorCode.FEED_CANNOT_ACCESS);
        }
    }

    /**
     * 팔로우 한사람이 새로운 게시글을 작성할 경우 알림
     *
     * @param actor  알림 발생자
     * @param feedId 알림 발생 피드
     */
    private void followerNewPostNotification(User actor, Long feedId) {
        List<FollowResponseDto> followers = followService.findFollowers();
        for (FollowResponseDto follower : followers) {
            User targetUser = userService.getUserById(follower.getUserId());
            String context = String.format(NotificationMessage.FOLLOW_FEED_NOTIFICATION, actor.getName());
            notificationService.createNotification(targetUser, actor, NotifyType.NEW_FEED, context, feedId);
        }
    }

    public String getRepresentativePhotoUrl(Long feedId) {
        // Feed에 속한 Days 리스트 조회
        List<Days> daysList = daysRepository.findByFeedId(feedId);

        // Days에 속한 Activity 리스트 조회 및 대표 사진 URL 추출
        return daysList.stream()
                .flatMap(day -> activityRepository.findByDays(day).stream()) // Activity 조회
                .flatMap(activity -> activity.getActivityPhotos().stream()) // ActivityPhoto 조회
                .map(activityPhoto -> activityPhoto.getPhoto().getUrl()) // URL 추출
                .findFirst()
                .orElse(null); // 대표 사진이 없으면 null 반환
    }
}
