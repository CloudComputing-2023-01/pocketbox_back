package store.pocketbox.app.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.security.jwt.Token;
import store.pocketbox.app.security.jwt.TokenService;
import store.pocketbox.app.web.dto.UserRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final TokenService tokenService;

    private Long getUserIdInHeader() {
        Long userIdString = Long.valueOf(request.getHeader("userId"));

        try {
            return userIdString;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }
    //userId 로
    public User getCurrentUser() {
        return userRepository.findById(getUserIdInHeader()).get();
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 유저를 찾을 수 없습니다."));
    }

    public User getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal().equals("anonymousUser")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"다시 로그인하세요");
        }
        return (User) authentication.getPrincipal();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 회원이 없습니다."));
    }

    public void deleteUser() {
        User user = getLoginUser();
        userRepository.delete(user);
    }
    public User updateUser(User user) {
        User currentUser = getLoginUser();

        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());

        userRepository.save(currentUser);
        return currentUser;
    }
    String verifyLogin(User user) {
        User validUser = getUserByEmail(user.getEmail());
        if (!Objects.equals(validUser.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"비밀번호를 잘못 입력했습니다.");
        }
        else {
            return tokenService.generateToken(validUser.getEmail(), "USER").getAccessToken();
        }
    }
    User registerUser(User user) {
        Optional<User> newUser = userRepository.findByEmail(user.getEmail());
        if (newUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"이미 존재하는 유저 email 입니다.");
        }
        else {
            userRepository.save(user);
            return user;
        }
    }
}
