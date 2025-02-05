package com.shineidle.tripf.domain.comment.repository;

import com.shineidle.tripf.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 피드에 속한 댓글 조회
     *
     * @param commentId 댓글식별자
     * @param feedId 피드식별자
     * @return {@link Comment}
     */
    Optional<Comment> findByIdAndFeedId(Long commentId, Long feedId);

    /**
     * 피드에 속한 모든 댓글 목록 조회
     *
     * @param feedId 피드식별자
     * @return {@link Comment}
     */
    List<Comment> findAllByFeedId(Long feedId);
}
