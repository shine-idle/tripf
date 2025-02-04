package com.shineidle.tripf.comment.repository;

import com.shineidle.tripf.comment.entity.Comment;
import com.shineidle.tripf.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // TODO : 주석 필요
    Optional<Comment> findByIdAndFeedId(Long commentId, Long feedId);

    List<Comment> findAllByFeedId(Long feedId);
}
