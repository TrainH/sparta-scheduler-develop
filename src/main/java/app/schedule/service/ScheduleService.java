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

    public ScheduleDto createSchedule(CreateScheduleRequest request, long userId) {
        User author = userRepository.findById(userId).orElseThrow(()-> new ValidateException("존재 하지 않는 유저 입니다.", HttpStatus.NOT_FOUND));
        Schedule schedule = Schedule.of(request,author);
        scheduleRepository.save(schedule);
        return convertToDto(schedule);
    }

    public ScheduleDto updateSchedule(long scheduleId, UpdateScheduleRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        if (!schedule.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this schedule.");
        }

        schedule.updateSchedule(request);

        scheduleRepository.save(schedule);

        return convertToDto(schedule);
    }

    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        if (!schedule.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this schedule");
        }
        scheduleRepository.deleteById(scheduleId);
    }

    private ScheduleDto convertToDto(Schedule schedule) {
        return ScheduleDto.convertDto(schedule);
    }


}
