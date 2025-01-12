package com.shineidle.tripf.comment.service;

import com.shineidle.tripf.comment.dto.CommentRequestDto;
import com.shineidle.tripf.comment.dto.CommentResponseDto;
import com.shineidle.tripf.comment.entity.Comment;
import com.shineidle.tripf.comment.repository.CommentRepository;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.CommentErrorCode;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.feed.service.FeedService;
import com.shineidle.tripf.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final FeedService feedService;

    /**
     * 댓글 작성
     *
     * @param feedId            피드 식별자
     * @param commentRequestDto 댓글 요청 DTO
     * @return CommentResponseDto.toDto
     */
    @Override
    public CommentResponseDto createComment(Long feedId, CommentRequestDto commentRequestDto) {
        User user = UserAuthorizationUtil.getLoginUser();
        Feed feed = checkFeed(feedId);

        Comment comment = new Comment(
                feed,
                user,
                commentRequestDto.getComment()
        );
        Comment savecomment = commentRepository.save(comment);

        return CommentResponseDto.toDto(savecomment);
    }

    /**
     * 댓글 수정
     * @param feedId 피드 식별자
     * @param commentId 댓글 식별자
     * @param commentRequestDto 댓글 요청 DTo
     * @return CommentResponseDto.toDto
     */
    @Override
    public CommentResponseDto updateComment(Long feedId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = checkComment(feedId, commentId);
        checkUser(feedId, commentId);

        comment.update(
                commentRequestDto.getComment()
        );

        commentRepository.save(comment);

        return CommentResponseDto.toDto(comment);
    }

    @Override
    public List<CommentResponseDto> findAllComment(Long feedId) {

        List<Comment> comments = commentRepository.findAllByFeedId(feedId);

        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostMessageResponseDto deleteComment(Long feedId, Long commentId) {
        checkUser(feedId, commentId);
        checkComment(feedId, commentId);
        commentRepository.deleteById(commentId);
        return new PostMessageResponseDto(PostMessage.COMMENT_DELETED);
    }

    /**
     * repository Service method
     */

    /**
     * 피드 Id로 피드 확인
     *
     * @param feedId 피드 식별자
     * @return feed
     */
    public Feed checkFeed(Long feedId) {
        return feedService.checkFeed(feedId);
    }

    /**
     * 피드Id에 속한 댓글인지 확인
     *
     * @param feedId    피드 식별자
     * @param commentId 댓글 식별자
     * @return comment
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

}
