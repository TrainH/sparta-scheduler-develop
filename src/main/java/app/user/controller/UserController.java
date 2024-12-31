package app.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.user.model.request.*;
import app.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody SignUpRequest request) {
        userService.createUser(request);
        return new ResponseEntity<>(" 등록되었습니다. ", HttpStatus.CREATED);
    }

    @PatchMapping("/deletion")
    public ResponseEntity<String> softDeleteUser(@Valid @RequestBody DeletionRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        userService.softDeleteUser(userId, request);
        session.invalidate();
        return new ResponseEntity<>("삭제 되었습니다. ", HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid@RequestBody PasswordRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        userService.updatePassword(userId, request.oldPassword(), request.newPassword());
        session.invalidate();
        return new ResponseEntity<>("비밀번호가 변경되었습니다. 다시 로그인 해주세요.", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> getUserId(@Valid @RequestBody LoginRequest request, HttpSession session) {
        Long userId = userService.getUserId(request);

        session.setAttribute("userId", userId);

        return new ResponseEntity<>("로그인 성공했습니다.", HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("로그아웃 성공했습니다.",HttpStatus.OK);
    }

}
