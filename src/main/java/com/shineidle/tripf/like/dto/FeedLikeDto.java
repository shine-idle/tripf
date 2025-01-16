package com.shineidle.tripf.like.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedLikeDto {

    private final Long feedId;

    private final String title;

    private final Long likeCount;

}
