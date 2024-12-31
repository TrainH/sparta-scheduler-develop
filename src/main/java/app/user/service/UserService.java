package app.user.service;

import app.common.config.encode.PasswordEncoder;
import app.common.exception.ValidateException;
import app.common.entity.User;
import app.user.model.request.SignUpRequest;
import app.user.model.request.LoginRequest;
import app.user.model.request.DeletionRequest;
import app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(SignUpRequest request) {
        userRepository.existsByEmailOrElseThrow(request.email());

        String encodePassword = passwordEncoder.encode(request.password());

        User user = User.createUser(request, encodePassword, false);

        userRepository.save(user);
    }

    @Transactional
    public void softDeleteUser(Long loginUserId, DeletionRequest request) {
        User user = userRepository.findByEmailOrElseThrow(request.email());

        user.validateUserId(loginUserId);

        user.validatePassword(request.password(), passwordEncoder);

        user.updateIsDeleted(true);

        userRepository.save(user);
    }

    public Long getUserId(LoginRequest request) {
        User user = userRepository.findByEmailOrElseThrow(request.email());

        user.validateIsDeleted();

        user.validatePassword(request.password(), passwordEncoder);

        return user.getUserId();
    }

    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword){
        User user = userRepository.findByUserIdOrElseThrow(userId);

        user.validatePassword(oldPassword, passwordEncoder);

        String encodePassword = passwordEncoder.encode(newPassword);

        user.updatePassword(encodePassword);

        userRepository.save(user);
    }
}
