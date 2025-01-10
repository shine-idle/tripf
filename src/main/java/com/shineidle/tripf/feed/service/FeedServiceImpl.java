package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FeedErrorCode;
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
import com.shineidle.tripf.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final DaysRepository daysRepository;
    private final ActivityRepository activityRepository;

    /**
     * 피드 생성
     *
     * @param feedRequestDto 피드 요청 Dto
     * @return feedResponseDto 피드 응답 Dto
     */
    @Override
    public FeedResponseDto createFeed(FeedRequestDto feedRequestDto) {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        Feed feed = new Feed(new User(userId), feedRequestDto.getCity(), feedRequestDto.getStartedAt(), feedRequestDto.getEndedAt(), feedRequestDto.getTitle(), feedRequestDto.getContent(), feedRequestDto.getCost(), feedRequestDto.getTag());
        Feed saveFeed = feedRepository.save(feed);

        List<DaysResponseDto> daysResponseDto = feedRequestDto.getDays() != null
                ? feedRequestDto.getDays()
                .stream()
                .map(daysRequestDto -> {
                    Days days = new Days(
                            saveFeed,
                            daysRequestDto.getDate()
                    );
                    Days savedDays = daysRepository.save(days);

                    List<ActivityResponseDto> activityResponseDto = daysRequestDto.getActivity() != null
                            ? daysRequestDto.getActivity()
                            .stream()
                            .map(activityRequestDto -> {
                                Activity activity = new Activity(
                                        savedDays,
                                        activityRequestDto.getTitle(),
                                        activityRequestDto.getStar(),
                                        activityRequestDto.getMemo(),
                                        activityRequestDto.getCity(),
                                        activityRequestDto.getLatitude(),
                                        activityRequestDto.getLongitude()
                                );
                                Activity savedActivity = activityRepository.save(activity);
                                return new ActivityResponseDto(
                                        savedActivity.getId(),
                                        savedActivity.getTitle(),
                                        savedActivity.getStar(),
                                        savedActivity.getMemo(),
                                        savedActivity.getCity(),
                                        savedActivity.getLatitude(),
                                        savedActivity.getLongitude()
                                );
                            }).toList()
                            : List.of();
                    return new DaysResponseDto(
                            savedDays.getId(),
                            savedDays.getDate(),
                            activityResponseDto
                    );
                }).toList()
                : List.of();

        return FeedResponseDto.toDto(saveFeed, daysResponseDto);
    }

    /**
     * 피드 수정
     *
     * @param feedId         피드 ID
     * @param feedRequestDto 피드 요청 DTO
     * @return feedResponseDto 수정된 피드 응답 DTO
     */
    @Override
    public FeedResponseDto updateFeed(Long feedId, FeedRequestDto feedRequestDto) {
        checkUser(feedId);
        Feed feed = checkFeed(feedId);
        feed.update(
                feedRequestDto.getCity(),
                feedRequestDto.getStartedAt(),
                feedRequestDto.getEndedAt(),
                feedRequestDto.getTitle(),
                feedRequestDto.getContent(),
                feedRequestDto.getCost(),
                feedRequestDto.getTag()
        );
        List<DaysResponseDto> daysResponseDto = daysRepository.findByFeed(feed).stream()
                .map(days -> {
                    List<ActivityResponseDto> activityResponseDto = activityRepository.findByDays(days)
                            .stream()
                            .map(activity -> new ActivityResponseDto(
                                    activity.getId(),
                                    activity.getTitle(),
                                    activity.getStar(),
                                    activity.getMemo(),
                                    activity.getCity(),
                                    activity.getLatitude(),
                                    activity.getLongitude()
                            )).toList();

                    return new DaysResponseDto(
                            days.getId(),
                            days.getDate(),
                            activityResponseDto
                    );
                }).toList();
        feedRepository.save(feed);
        return FeedResponseDto.toDto(feed, daysResponseDto);
    }

    /**
     * 피드 상세조회
     *
     * @param feedId 피드 식별자
     * @return FeedResponseDto
     */
    @Override
    public FeedResponseDto findFeed(Long feedId) {
        Feed feed = checkFeed(feedId);

        List<Days> daysList = daysRepository.findByFeed(feed);

        List<DaysResponseDto> daysResponseDto = daysList.stream()
                .map(days -> {
                    List<ActivityResponseDto> activityResponseDto = activityRepository.findByDays(days)
                            .stream()
                            .map(activity -> new ActivityResponseDto(
                                    activity.getId(),
                                    activity.getTitle(),
                                    activity.getStar(),
                                    activity.getMemo(),
                                    activity.getCity(),
                                    activity.getLatitude(),
                                    activity.getLongitude()
                            )).toList();
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
     * @return PostMessageResponseDto(PostMessage.PEED_DELETED)
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
     * @return new PostMessageResponseDto(PostMessage.DAYS_DELETED);
     */
    @Override
    public PostMessageResponseDto deleteDays(Long feedId, Long daysId) {
        checkUser(feedId);
        checkDays(feedId, daysId);
        daysRepository.deleteById(daysId);
        return new PostMessageResponseDto(PostMessage.DAYS_DELETED);
    }

    /**
     * 활동 삭제
     *
     * @param feedId 피드 식별자
     * @param daysId 일정 식별자
     * @param activityId 활동 식별자
     * @return PostMessageResponseDto(PostMessage.ACTIVITY_DELETED);
     */
    @Override
    public PostMessageResponseDto deleteActivity(Long feedId, Long daysId, Long activityId) {
        checkUser(feedId);
        checkActivity(feedId, daysId, activityId);
        activityRepository.deleteById(activityId);
        return new PostMessageResponseDto(PostMessage.ACTIVITY_DELETED);
    }

    /**
     * 일정 추가
     * 일정 추가 시 활동도 같이 추가
     */
    @Override
    public FeedResponseDto createDay(Long feedId, DaysRequestDto daysRequestDto) {
        return null;
    }

    /**
     * 일정, 활동 수정
     */
    @Override
    public FeedResponseDto updateDay(Long feedId, DaysRequestDto daysRequestDto) {
        return null;
    }

    /**
     * 국가별 피드 조회
     */
    @Override
    public List<RegionResponseDto> findRegion(String city) {
        return List.of();
    }

    /**
     * repository Service method
     */

    /**
     * 피드 Id로 피드 확인
     *
     * @param feedId 피드 식별자
     * @return feed
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
     * @return days
     */
    public Days checkDays(Long feedId, Long daysId) {
        return daysRepository.findByIdWithFeed(feedId, daysId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.DAYS_NOT_FOUND));
    }

    /**
     * 피드 및 일정 및 피드 Id로 활동 확인
     *
     * @param feedId 피드 식별자
     * @param daysId 일정 식별자
     * @param activityId 활동 식별자
     * @return Activity
     */
    public Activity checkActivity(Long feedId, Long daysId, Long activityId) {
        return activityRepository.findByIdWithFeedAndDays(feedId, daysId, activityId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.ACTIVITY_NOT_FOUND));
    }

    /**
     * 작성자 검증 method
     * 로그인한 유저와 작성자가 다를 경우 exception
     * @param feedId
     */
    public void checkUser(Long feedId) {
        Feed checkFeed = checkFeed(feedId);
        Long checkUser = UserAuthorizationUtil.getLoginUserId();

        if (!checkFeed.getUser().getId().equals(checkUser)) {
            throw new GlobalException(FeedErrorCode.FEED_CANNOT_ACCESS);
        }
    }
}