package store.pocketbox.app.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.exception.ResponseMessage;
import store.pocketbox.app.exception.StatusCode;
import store.pocketbox.app.service.UserService;
import store.pocketbox.app.web.dto.PostResponseDto;
import store.pocketbox.app.web.dto.UserRequestDto;
import store.pocketbox.app.web.dto.UserResponseDto;
import store.pocketbox.app.web.dto.base.DefaultRes;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<DefaultRes<UserResponseDto.Info>> getMyProfile() throws Exception {
        return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_READ_ORDER_BY_LIKE_SUCCESS, UserResponseDto.Info.of(userService.getLoginUser())), HttpStatus.OK);
    }

    @PutMapping("/info")
    public ResponseEntity<DefaultRes<UserResponseDto.Info>> updateProfile(UserRequestDto.Register register) throws Exception {
        return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_READ_ORDER_BY_LIKE_SUCCESS, UserResponseDto.Info.of(userService.updateUser(register.toEntity()))), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<DefaultRes<UserResponseDto.AccessToken>> login(UserRequestDto.Login login) throws Exception {
        return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_READ_ORDER_BY_LIKE_SUCCESS, UserResponseDto.AccessToken.of(userService.verifyLogin(login.toEntity()))), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<DefaultRes<UserResponseDto.Info>> register(UserRequestDto.Register register) throws Exception {
        return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_READ_ORDER_BY_LIKE_SUCCESS, UserResponseDto.Info.of(userService.registerUser(register.toEntity()))), HttpStatus.OK);
    }
}
