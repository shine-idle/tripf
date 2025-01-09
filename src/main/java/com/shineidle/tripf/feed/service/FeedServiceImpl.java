package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FeedErrorCode;
import com.shineidle.tripf.feed.dto.*;
import com.shineidle.tripf.feed.entity.Activity;
import com.shineidle.tripf.feed.entity.Days;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.feed.repository.ActivityRepository;
import com.shineidle.tripf.feed.repository.DaysRepository;
import com.shineidle.tripf.feed.repository.FeedRepository;
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
        Feed feed = new Feed(feedRequestDto.getCity(), feedRequestDto.getStarted_at(), feedRequestDto.getEnded_at(), feedRequestDto.getTitle(), feedRequestDto.getContent(), feedRequestDto.getCost(), feedRequestDto.getTag());
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

                    List<ActivityResponseDto> activityResponseDto = daysRequestDto.getActivity() !=null
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
                        :List.of();
                    return new DaysResponseDto(
                            savedDays.getId(),
                            savedDays.getDate(),
                            activityResponseDto
                    );
                }).toList()
        :List.of();

        return FeedResponseDto.toDto(saveFeed, daysResponseDto);
    }

    // 피드 수정
    @Override
    public FeedResponseDto updateFeed(Long feedId, FeedRequestDto feedRequestDto) {
        Feed feed = findByIdOrElseThrow(feedId);

        Feed updateFeed = feed.update(feedRequestDto.getCity(), feedRequestDto.getStarted_at(), feedRequestDto.getEnded_at(), feedRequestDto.getTitle(), feedRequestDto.getContent(), feedRequestDto.getCost(), feedRequestDto.getTag());
        Feed saveFeed = feedRepository.save(updateFeed);

        List<DaysResponseDto> daysResponseDto = feedRequestDto.getDays() != null
                ? feedRequestDto.getDays()
                .stream()
                .map(daysRequestDto -> {
                    Days days = new Days(
                            saveFeed,
                            daysRequestDto.getDate()
                    );
                    Days updateDays = days.update(saveFeed, daysRequestDto.getDate());

                    List<ActivityResponseDto> activityResponseDto = daysRequestDto.getActivity() !=null
                            ? daysRequestDto.getActivity()
                            .stream()
                            .map(activityRequestDto -> {
                                Activity activity = new Activity(
                                        updateDays,
                                        activityRequestDto.getTitle(),
                                        activityRequestDto.getStar(),
                                        activityRequestDto.getMemo(),
                                        activityRequestDto.getCity(),
                                        activityRequestDto.getLatitude(),
                                        activityRequestDto.getLongitude()
                                );
                                Activity updateActivity = activity.update(updateDays, activityRequestDto.getTitle(), activityRequestDto.getStar(), activityRequestDto.getMemo(),activityRequestDto.getCity(),activityRequestDto.getLatitude(),activityRequestDto.getLongitude());
                                return new ActivityResponseDto(
                                        updateActivity.getId(),
                                        updateActivity.getTitle(),
                                        updateActivity.getStar(),
                                        updateActivity.getMemo(),
                                        updateActivity.getCity(),
                                        updateActivity.getLatitude(),
                                        updateActivity.getLongitude()
                                );
                            }).toList()
                            :List.of();
                    return new DaysResponseDto(
                            updateDays.getId(),
                            updateDays.getDate(),
                            activityResponseDto
                    );
                }).toList()
                :List.of();

        return FeedResponseDto.toDto(saveFeed, daysResponseDto);

    }

    // 피드 상세 조회
    @Override
    public FeedResponseDto findFeed(Long feedId) {
        return null;
    }

    // 피드 삭제
    @Override
    public String deleteFeed(Long feedId) {
        return "";
    }

    // 활동 삭제
    @Override
    public String deleteActivity(Long feedId, Long daysId, Long activityId) {
        return "";
    }

    // 피드 findById
    public Feed findByIdOrElseThrow(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.FEED_NOT_FOUND));
    }

    // 일정 findById
    public Days findByIdOrElseThrow(Long feedId, Long daysId) {
        return daysRepository.findByFeedIdAndDaysId(feedId,daysId)
                .orElseThrow(() -> new GlobalException(FeedErrorCode.FEED_NOT_FOUND));
    }
}