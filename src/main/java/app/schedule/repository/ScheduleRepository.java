package app.schedule.repository;

import app.common.entity.Schedule;
import app.schedule.model.dto.SchedulePageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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

}
