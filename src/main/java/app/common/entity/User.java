package app.common.entity;

import app.common.config.encode.PasswordEncoder;
import app.common.exception.ValidateException;
import app.user.model.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean isDeleted;

    public static User createUser(SignUpRequest request, String encodePassword, boolean isDeleted) {
        return new User(null, request.userName(), request.email(), encodePassword, isDeleted);
    }


    public void validateUserId(Long loginUserId) {
        if (!this.userId.equals(loginUserId)) {
            throw new ValidateException("본인 계정만 탈퇴 가능합니다.", HttpStatus.CONFLICT);
        }
    }


    public void validatePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new ValidateException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }


    public void validateIsDeleted() {
        if(this.isDeleted){
            throw new ValidateException("계정은 이미 삭제 되었습니다.", HttpStatus.CONFLICT);
        }
    }


    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public void updatePassword(String password) {
        this.password = password;
    }
}
