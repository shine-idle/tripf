package com.shineidle.tripf.like.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.LikeErrorCode;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.feed.service.FeedService;
import com.shineidle.tripf.like.entity.Like;
import com.shineidle.tripf.like.entity.LikePk;
import com.shineidle.tripf.like.repository.LikeRepository;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class LikeServieImpl implements LikeServie{
    private final LikeRepository likeRepository;
    private final FeedService feedService;
    private final UserService userService;

    /**
     * 좋이요
     * @param feedId 좋이요를 누를 피드 Id
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
     }

    /**
     * 좋아요 취소
     * @param feedId 좋아요를 취소할 피드 Id
     */
     @Override
     public void deleteLike(Long feedId) {
         User loginUser = UserAuthorizationUtil.getLoginUser();
         Feed feed = feedService.checkFeed(feedId);

         LikePk likePk = new LikePk(feed, loginUser);

         if (!likeRepository.existsById(likePk)) {
            throw new GlobalException(LikeErrorCode.LIKED_YET);
         }

         loginUser = userService.getUserById(loginUser.getId());

         likeRepository.deleteById(likePk);
     }
}