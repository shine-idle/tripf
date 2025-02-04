package com.shineidle.tripf.follow.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// TODO: 사용되지 않는 Dto 삭제 유무
@Getter
@RequiredArgsConstructor
public class FollowRequestDto {
    private final Long followingId;
}
