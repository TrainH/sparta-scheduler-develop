package app.common.entity;

import app.comment.model.request.CreateCommentRequest;
import app.comment.model.request.UpdateCommentRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public static Comment of(CreateCommentRequest request, User user, Schedule schedule) {
        return new Comment(null, request.content(), user, schedule);
    }

    public void updateComment(UpdateCommentRequest request) {
        this.content = request.content();
    }

}
