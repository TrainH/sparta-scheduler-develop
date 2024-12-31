package app.comment.model.dto;

import app.common.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentPageDto {
    private Long commentId;
    private Long userId;
    private Long scheduleId;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    public CommentPageDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUser().getUserId();
        this.scheduleId = comment.getSchedule().getScheduleId();
        this.content = comment.getContent();
        this.createAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
