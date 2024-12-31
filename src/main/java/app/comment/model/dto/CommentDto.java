package app.comment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.common.entity.Comment;
import app.schedule.model.dto.ScheduleDto;
import app.user.model.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long commentId;
    private String content;
    private UserDto user;
    private ScheduleDto schedule;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentDto convertDto(Comment comment) {
        return new CommentDto(comment.getCommentId(),
            comment.getContent(),
            UserDto.convertDto(comment.getUser()),
            ScheduleDto.convertDto(comment.getSchedule()),
            comment.getCreatedAt(),
            comment.getUpdatedAt()
            );
    }
}
