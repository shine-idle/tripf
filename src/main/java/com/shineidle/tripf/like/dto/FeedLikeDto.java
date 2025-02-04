package com.shineidle.tripf.like.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// TODO: 사용되지 않는 메서드 삭제 유무
@Getter
@RequiredArgsConstructor
public class FeedLikeDto {
    private final Long activityId;

    private final String title;

    private final Long likeCount;

    private final List<String> photoUrls;

    public FeedLikeDto(Long activityId, String title, Long likeCount) {
        this.activityId = activityId;
        this.title = title;
        this.likeCount = likeCount;
        this.photoUrls = new ArrayList<>();
    }

    public String getRepresentativeImageUrl() {
        if (photoUrls != null && !photoUrls.isEmpty()) {
            return photoUrls.get(0);
        }
        return "cid:defaultImage";
    }
}