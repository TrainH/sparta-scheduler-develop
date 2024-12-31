package app.comment.model.request;


import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
    @NotBlank(message = "댓글 내용이 없습니다.")
    String content
) {

}
