package app.comment.service;

import app.comment.model.dto.CommentPageDto;
import lombok.RequiredArgsConstructor;
import app.comment.model.dto.CommentDto;
import app.comment.model.request.CreateCommentRequest;
import app.comment.model.request.UpdateCommentRequest;
import app.comment.repository.CommentRepository;
import app.common.entity.Comment;
import app.common.entity.Schedule;
import app.common.entity.User;
import app.common.exception.ValidateException;
import app.schedule.repository.ScheduleRepository;
import app.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public Page<CommentPageDto> getCommentsByScheduleId(Long userId, Pageable pageable) {
        return commentRepository.findCommentsByUserId(userId, pageable);
    }


    public CommentDto createComment(Long userId, Long scheduleId, CreateCommentRequest request) {
        User user = userRepository.findByUserIdOrElseThrow(userId);

        Schedule schedule = scheduleRepository.findByScheduleIdOrElseThrow(scheduleId);

        Comment comment = Comment.of(request, user, schedule);

        commentRepository.save(comment);

        return convertDto(comment);
    }


    public CommentDto updateComment(Long userId, Long scheduleId, Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findByCommentIdOrElseThrow(commentId);

        comment.validateUserId(userId);

        comment.updateComment(request);

        commentRepository.save(comment);

        return convertDto(comment);
    }


    public void deleteComment(Long userId, Long scheduleId, Long commentId) {
        Comment comment = commentRepository.findByCommentIdOrElseThrow(commentId);

        comment.validateUserId(userId);

        commentRepository.deleteById(commentId);
    }

    private CommentDto convertDto(Comment comment) {return CommentDto.convertDto(comment); }
}
