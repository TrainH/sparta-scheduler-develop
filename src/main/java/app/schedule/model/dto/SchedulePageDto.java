package app.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePageDto {
    private Long scheduleId;
    private String title;
    private String content;
    private String userName;
    private Long commentCount; // 댓글 개수
    private LocalDateTime updatedAt;
}
