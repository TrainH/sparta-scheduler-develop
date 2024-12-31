package app.schedule.repository;

import app.common.entity.Schedule;
import app.common.entity.User;
import app.common.exception.ValidateException;
import app.schedule.model.dto.SchedulePageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT new app.schedule.model.dto.SchedulePageDto(" +
            "s.scheduleId, s.title, s.content, u.userName, COUNT(c.commentId), s.updatedAt)" +
            "FROM Schedule s " +
            "LEFT JOIN s.user u " +
            "LEFT JOIN Comment c ON c.schedule = s " +
            "WHERE (:userId IS NULL OR u.userId = :userId) " + // userId가 null이면 모든 데이터 조회
            "GROUP BY s.scheduleId, s.title, s.content, u.userName, s.updatedAt " +
            "ORDER BY s.updatedAt DESC"
    )
    Page<SchedulePageDto> findSchedulesByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<Schedule> findByScheduleId(Long scheduleId);

    default Schedule findByScheduleIdOrElseThrow(Long scheduleId) {
        return findByScheduleId(scheduleId).orElseThrow(() -> new ValidateException("일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }
}
