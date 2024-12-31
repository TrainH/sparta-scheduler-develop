package app.schedule.model.request;


public record UpdateScheduleRequest(
    String title,
    String content
) {
}
