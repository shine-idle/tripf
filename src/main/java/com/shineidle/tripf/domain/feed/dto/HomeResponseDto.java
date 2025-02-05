package com.shineidle.tripf.domain.feed.dto;

import com.shineidle.tripf.domain.follow.dto.FollowResponseDto;
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

    // 빈 팔로워 설정
    public HomeResponseDto withEmptyFollowers() {
        return new HomeResponseDto(this.korea, this.global, List.of(), this.followings);
    }

    // 빈 팔로잉 설정
    public HomeResponseDto withEmptyFollowings() {
        return new HomeResponseDto(this.korea, this.global, this.followers, List.of());
    }

}
