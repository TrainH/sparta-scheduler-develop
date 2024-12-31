package app.schedule.service;


import app.common.exception.ValidateException;
import app.schedule.model.request.UpdateScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import app.common.entity.Schedule;
import app.common.entity.User;
import app.schedule.model.dto.ScheduleDto;
import app.schedule.model.dto.SchedulePageDto;
import app.schedule.model.request.CreateScheduleRequest;
import app.schedule.repository.ScheduleRepository;
import app.user.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;


    public Page<SchedulePageDto> getSchedules(Long userId, Pageable pageable) {
        return scheduleRepository.findSchedulesByUserId(userId, pageable);
    }


    public ScheduleDto createSchedule(Long userId, CreateScheduleRequest request) {

        User user = userRepository.findByUserIdOrElseThrow(userId);

        Schedule schedule = Schedule.of(request, user);

        scheduleRepository.save(schedule);

        return convertToDto(schedule);
    }


    public ScheduleDto updateSchedule(Long userId, Long scheduleId, UpdateScheduleRequest request) {

        Schedule schedule = scheduleRepository.findByScheduleIdOrElseThrow(scheduleId);

        schedule.validateUserId(userId);

        schedule.updateSchedule(request);

        scheduleRepository.save(schedule);

        return convertToDto(schedule);
    }


    public void deleteSchedule(Long userId, Long scheduleId) {

        Schedule schedule = scheduleRepository.findByScheduleIdOrElseThrow(scheduleId);

        schedule.validateUserId(userId);

        scheduleRepository.deleteById(scheduleId);
    }


    private ScheduleDto convertToDto(Schedule schedule) {
        return ScheduleDto.convertDto(schedule);
    }

}
