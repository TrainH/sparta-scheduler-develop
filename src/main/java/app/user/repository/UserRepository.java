package app.user.repository;

import app.common.exception.ValidateException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import app.common.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);

    default User findByUserIdOrElseThrow(Long userId) {
        return findByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재 하지 않는 유저 입니다. "));
    }


    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new ValidateException("계정이 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }


    boolean existsByEmail(String email);

    default void existsByEmailOrElseThrow(String email) {
        boolean isEmailExists = existsByEmail(email);
        if(isEmailExists) {
            throw new ValidateException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }
    }
}
