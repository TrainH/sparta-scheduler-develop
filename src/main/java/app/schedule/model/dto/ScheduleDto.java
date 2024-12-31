package app.schedule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.common.entity.Schedule;
import app.user.model.dto.UserDto;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {

    private Long scheduleId;
    private String title;
    private String content;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // TODO :: 정적 팩토리 메서드를 사용하면 어떤 장점이 있을까요?
    public static ScheduleDto convertDto(Schedule schedule) {
        return new ScheduleDto(schedule.getScheduleId(),
            schedule.getTitle(),
            schedule.getContent(),
            UserDto.convertDto(schedule.getUser()),
            schedule.getCreatedAt(),
            schedule.getUpdatedAt()
        );
    }
}