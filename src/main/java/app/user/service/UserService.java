package app.user.service;

import app.common.config.encode.PasswordEncoder;
import app.common.exception.ValidateException;
import app.common.entity.User;
import app.user.model.dto.UserDto;
import app.user.model.request.SignUpRequest;
import app.user.model.request.LoginRequest;
import app.user.model.request.DeletionRequest;
import app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(SignUpRequest request) {
        String encodePassword = passwordEncoder.encode(request.password());
        boolean isEmailExists = userRepository.existsByEmail(request.email());
        if(isEmailExists) {
            throw new ValidateException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }
        User user = User.createUser(request, encodePassword, false); // isDeleted 처음에 false
        userRepository.save(user);
        UserDto.convertDto(user);
    }

    public void softDeleteUser(Long userId, DeletionRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ValidateException("계정이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        if (!user.getUserId().equals(userId)) {
            throw new ValidateException("본인 계정만 탈퇴 가능합니다.", HttpStatus.CONFLICT);
        }

        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            user.updateIsDeleted(true);
            userRepository.save(user);
        } else {
            throw new ValidateException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public Long getUserId(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ValidateException("계정이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        if(user.isDeleted()){
            throw new ValidateException("계정은 이미 삭제 되었습니다.", HttpStatus.CONFLICT);
        }

        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            return user.getUserId();
        } else {
            throw new ValidateException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public void updatePassword(Long userId, String oldPassword, String newPassword){
        User user = userRepository.findByUserIdOrElseThrow(userId);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new ValidateException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }else if(passwordEncoder.matches(newPassword, user.getPassword())){
            throw new ValidateException("현재 비밀번호로와 동일한 비밀번호로는 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String encodePassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodePassword);
        userRepository.save(user);
    }

}
