package com.shineidle.tripf.feed.dto;

import com.shineidle.tripf.follow.dto.FollowResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class HomeResponseDto {

    private final List<RegionResponseDto> korea;

    private final List<RegionResponseDto> global;

    private final List<FollowResponseDto> followers;

    private final List<FollowResponseDto> followings;

}
