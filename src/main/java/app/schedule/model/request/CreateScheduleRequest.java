package app.schedule.model.request;

import jakarta.validation.constraints.NotBlank;

public record CreateScheduleRequest(
    @NotBlank(message = "titile은 공백일 수 없습니다. ")
    String title,
    @NotBlank(message = "Content는 공백일 수 없습니다.")
    String content
) {
}
