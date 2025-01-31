package com.shineidle.tripf.like.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.LikeErrorCode;
import com.shineidle.tripf.common.message.constants.NotificationMessage;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.feed.service.FeedService;
import com.shineidle.tripf.like.dto.FeedLikeDto;
import com.shineidle.tripf.like.entity.Like;
import com.shineidle.tripf.like.entity.LikePk;
import com.shineidle.tripf.like.repository.LikeRepository;
import com.shineidle.tripf.notification.service.NotificationService;
import com.shineidle.tripf.notification.type.NotifyType;
import com.shineidle.tripf.photo.service.PhotoService;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final FeedService feedService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final PhotoService photoService;

    /**
     * 좋아요
     *
     * @param feedId 피드 식별자(좋아요를 누를 피드 식별자)
     */
    @Override
    public void createLike(Long feedId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();

        Feed feed = feedService.checkFeed(feedId);

        LikePk likePk = new LikePk(feed, loginUser);
        if (likeRepository.existsById(likePk)) {
            throw new GlobalException(LikeErrorCode.ALREADY_LIKE);
        }

        loginUser = userService.getUserById(loginUser.getId());

        Like like = new Like(feed, loginUser);
        likeRepository.save(like);

        createLikeNotification(feed.getUser(), loginUser, feedId);
    }

    /**
     * 좋아요 취소
     *
     * @param feedId 피드 식별자(좋아요를 취소할 피드 식별자)
     */
    @Override
    public void deleteLike(Long feedId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Feed feed = feedService.checkFeed(feedId);

        LikePk likePk = new LikePk(feed, loginUser);

        if (!likeRepository.existsById(likePk)) {
            throw new GlobalException(LikeErrorCode.LIKED_YET);
        }

        likeRepository.deleteById(likePk);
    }

    /**
     * 좋아요 상위 5개 피드 반환
     *
     * @return findTop5FeedsWithLikeCount
     */
    public List<FeedLikeDto> getTop5LikedFeedsWithImages() {
        Pageable pageable = PageRequest.of(0, 5);
        List<FeedLikeDto> topFeeds = likeRepository.findTop5FeedsWithLikeCount(pageable);

        return topFeeds.stream()
                .map(feed -> new FeedLikeDto(
                        feed.getActivityId(),
                        feed.getTitle(),
                        feed.getLikeCount(),
                        photoService.getActivityPhotoUrls(feed.getActivityId())
                ))
                .toList();
    }

    /**
     * 피드에 좋아요를 남길 경우 알림
     *
     * @param targetUser 알림 수신자 (피드 소유자)
     * @param actor      알림 발생자 (좋아요 누른 사람)
     * @param feedId     피드 식별자
     */
    private void createLikeNotification(User targetUser, User actor, Long feedId) {
        String context = String.format(NotificationMessage.LIKE_NOTIFICATION, actor.getName());
        notificationService.createNotification(targetUser, actor, NotifyType.LIKE, context, feedId);
    }
}