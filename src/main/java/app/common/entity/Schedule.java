package app.common.entity;

import app.common.exception.ValidateException;
import app.schedule.model.request.CreateScheduleRequest;
import app.schedule.model.request.UpdateScheduleRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "schedule")
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Schedule(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Schedule of(CreateScheduleRequest request, User user) {
        return Schedule.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();
    }

    public void updateSchedule(UpdateScheduleRequest request) {
        if(request.title() != null) {
            this.title = request.title();
        }
        if(request.content() != null) {
            this.content = request.content();
        }
    }

    public void validateUserId(Long loginUserId) {
        if (!this.getUser().getUserId().equals(loginUserId)) {
            throw new ValidateException("권한이 없습니다.",HttpStatus.UNAUTHORIZED);
        }
    }
}