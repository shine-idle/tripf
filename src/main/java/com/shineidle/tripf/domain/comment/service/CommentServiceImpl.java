package com.shineidle.tripf.domain.comment.service;

import com.shineidle.tripf.domain.comment.dto.CommentRequestDto;
import com.shineidle.tripf.domain.comment.dto.CommentResponseDto;
import com.shineidle.tripf.domain.comment.entity.Comment;
import com.shineidle.tripf.domain.comment.repository.CommentRepository;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.CommentErrorCode;
import com.shineidle.tripf.global.common.exception.type.LockErrorCode;
import com.shineidle.tripf.global.common.message.constants.NotificationMessage;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.global.common.message.type.PostMessage;
import com.shineidle.tripf.global.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.feed.service.FeedService;
import com.shineidle.tripf.domain.notification.service.NotificationService;
import com.shineidle.tripf.domain.notification.type.NotifyType;
import com.shineidle.tripf.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final FeedService feedService;
    private final NotificationService notificationService;
    private final RedissonClient redissonClient;

    /**
     * 댓글 작성
     *
     * @param feedId            피드 식별자
     * @param commentRequestDto {@link CommentRequestDto} 댓글 요청 Dto
     * @return {@link CommentResponseDto} 댓글 응답 Dto
     */
    @Override
    public CommentResponseDto createComment(Long feedId, CommentRequestDto commentRequestDto) {
        User user = UserAuthorizationUtil.getLoginUser();
        Feed feed = checkFeed(feedId);

        String lockKey = "createFeed:lock:user:" + user.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                Comment comment = new Comment(
                        feed,
                        user,
                        commentRequestDto.getComment()
                );
                Comment savecomment = commentRepository.save(comment);

                createCommentNotification(feed.getUser(), user, feedId);

                return CommentResponseDto.toDto(savecomment);
            } else {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 댓글 수정
     *
     * @param feedId            피드 식별자
     * @param commentId         댓글 식별자
     * @param commentRequestDto {@link CommentRequestDto} 댓글 요청 Dto
     * @return {@link CommentResponseDto} 댓글 응답 Dto
     */
    @Override
    public CommentResponseDto updateComment(Long feedId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = checkComment(feedId, commentId);
        checkUser(feedId, commentId);

        comment.update(commentRequestDto.getComment());

        commentRepository.save(comment);

        return CommentResponseDto.toDto(comment);
    }

    /**
     * 댓글 다건 조회
     *
     * @param feedId 피드 식별자
     * @return {@link CommentResponseDto} 댓글 응답 Dto
     */
    @Override
    public List<CommentResponseDto> findAllComment(Long feedId) {

        List<Comment> comments = commentRepository.findAllByFeedId(feedId);

        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     *
     * @param feedId    피드 식별자
     * @param commentId 댓글 식별자
     * @return {@link PostMessageResponseDto} 댓글 삭제 문구
     */
    @Override
    public PostMessageResponseDto deleteComment(Long feedId, Long commentId) {
        checkUser(feedId, commentId);
        checkComment(feedId, commentId);
        commentRepository.deleteById(commentId);
        return new PostMessageResponseDto(PostMessage.COMMENT_DELETED);
    }

    /**
     * 피드 Id로 피드 확인
     *
     * @param feedId 피드 식별자
     * @return {@link Feed}
     */
    public Feed checkFeed(Long feedId) {
        return feedService.checkFeed(feedId);
    }

    /**
     * 피드Id에 속한 댓글인지 확인
     *
     * @param feedId    피드 식별자
     * @param commentId 댓글 식별자
     * @return {@link Comment}
     */
    public Comment checkComment(Long feedId, Long commentId) {
        Comment comment = commentRepository.findByIdAndFeedId(commentId, feedId)
                .orElseThrow(() -> new GlobalException(CommentErrorCode.COMMENT_NOT_FOUND));

        return comment;
    }

    /**
     * 작성자 검증 method
     * 로그인한 유저와 작성자가 다를 경우 exception
     *
     * @param commentId 댓글 식별자
     */
    public void checkUser(Long feedId, Long commentId) {
        Comment checkComment = checkComment(feedId, commentId);
        Long checkUser = UserAuthorizationUtil.getLoginUserId();

        if (!checkComment.getUser().getId().equals(checkUser)) {
            throw new GlobalException(CommentErrorCode.COMMENT_CANNOT_ACCESS);
        }
    }

    /**
     * 댓글을 달 경우 알림 발생
     *
     * @param targetUser 알림 수신자 (알림 조회자)
     * @param actor      알림 발생자
     */
    private void createCommentNotification(User targetUser, User actor, Long feedId) {
        String context = String.format(NotificationMessage.NEW_COMMENT_NOTIFICATION, actor.getName());
        notificationService.createNotification(targetUser, actor, NotifyType.NEW_COMMENT, context, feedId);
    }
}