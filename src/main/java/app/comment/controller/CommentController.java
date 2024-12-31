package app.comment.controller;

import app.comment.model.dto.CommentDto;
import app.comment.model.dto.CommentPageDto;
import app.comment.model.request.CreateCommentRequest;
import app.comment.model.request.UpdateCommentRequest;
import app.comment.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{scheduleId}/comments")
    public ResponseEntity<Page<CommentPageDto>> getCommentsByScheduleId(
                                                              @PathVariable Long scheduleId,
                                                              @RequestParam(defaultValue = "5") int size,
                                                              @RequestParam(defaultValue = "1") int page) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return new ResponseEntity<>(commentService.getCommentsByScheduleId(scheduleId, pageable), HttpStatus.OK);
    }


    @PostMapping("/{scheduleId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long scheduleId,
                                                    @Valid @RequestBody CreateCommentRequest request,
                                                    HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        return new ResponseEntity<>(commentService.createComment(userId, scheduleId, request), HttpStatus.OK);
    }


    @PutMapping("/{scheduleId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long scheduleId,
                                                    @PathVariable Long commentId,
                                                    @Valid @RequestBody UpdateCommentRequest request,
                                                    HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        return new ResponseEntity<>(commentService.updateComment(userId, scheduleId, commentId, request), HttpStatus.OK);
    }


    @DeleteMapping("/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long scheduleId,
                                              @PathVariable Long commentId,
                                              HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        commentService.deleteComment(userId, scheduleId, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
