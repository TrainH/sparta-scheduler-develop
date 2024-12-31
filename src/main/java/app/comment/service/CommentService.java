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

    public CommentDto createComment(Long scheduleId, CreateCommentRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ValidateException("존재 하지 않는 유저 입니다.", HttpStatus.NOT_FOUND));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new ValidateException("존재 하지 않는 게시글 입니다.", HttpStatus.NOT_FOUND));
        Comment comment = Comment.of(request,user,schedule);
        commentRepository.save(comment);
        return convertDto(comment);
    }

    public CommentDto updateComment(Long commentId, UpdateCommentRequest request, Long userId) {
        Comment comment = findCommentById(commentId);
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new ValidateException("댓글 업데이트에 대한 권한을 가지고 있지않습니다.", HttpStatus.CONFLICT);
        }

        comment.updateComment(request);
        commentRepository.save(comment);
        return convertDto(comment);
    }

    public void deleteComment(Long scheduleId, Long commentId, Long userId) {
        Comment comment = findCommentById(commentId);
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new ValidateException("댓글 삭제에 대한 권한을 가지고 있지않습니다.", HttpStatus.CONFLICT);
        }
        commentRepository.deleteById(commentId);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(()-> new ValidateException("존재 하지 않는 댓글 입니다.", HttpStatus.NOT_FOUND));
    }

    private CommentDto convertDto(Comment comment) {return CommentDto.convertDto(comment); }
}
