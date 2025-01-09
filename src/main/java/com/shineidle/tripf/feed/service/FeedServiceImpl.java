package com.shineidle.tripf.feed.service;

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

                    return new DaysResponseDto(savedDays.getId(), savedDays.getDate(), activityResponseDto);
                }).toList()
        :List.of();

        return FeedResponseDto.toDto(saveFeed, daysResponseDto);
    }

    // 피드 생성
    @Override
    public FeedResponseDto updateFeed(FeedRequestDto feedRequestDto) {
        return null;
    }
}
