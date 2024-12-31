package app.schedule.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import app.schedule.model.dto.ScheduleDto;
import app.schedule.model.dto.SchedulePageDto;
import app.schedule.model.request.CreateScheduleRequest;
import app.schedule.model.request.UpdateScheduleRequest;
import app.schedule.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Page<SchedulePageDto>> getScheduleByUserId(
        @RequestParam(required = false) Long userId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return new ResponseEntity<>(scheduleService.getSchedules(userId, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody CreateScheduleRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(scheduleService.createSchedule(request, userId),HttpStatus.OK);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long scheduleId,@Valid @RequestBody UpdateScheduleRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(scheduleService.updateSchedule(scheduleId,request,userId),HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        scheduleService.deleteSchedule(scheduleId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
