package app.comment.repository;

import app.comment.model.dto.CommentPageDto;
import app.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new app.comment.model.dto.CommentPageDto(" +
            "c.commentId, c.user.userId, c.schedule.scheduleId, c.content, c.createdAt, c.updatedAt) " +
            "FROM Comment c " +
            "WHERE (:userId IS NULL OR c.user.userId = :userId) " + // userId가 null이면 모든 데이터 조회
            "ORDER BY c.updatedAt DESC")
    Page<CommentPageDto> findCommentsByUserId(@Param("userId") Long userId, Pageable pageable);
}