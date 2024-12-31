package app.comment.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
    @NotNull(message = "스케쥴 ID가 없습니다.")
    Long scheduleId,
    @NotBlank(message = "댓글 내용이 없습니다.")
    String content
) {

}
