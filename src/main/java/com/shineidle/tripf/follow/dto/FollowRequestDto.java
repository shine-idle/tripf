package com.shineidle.tripf.follow.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowRequestDto {
    private final Long followingId;
}
