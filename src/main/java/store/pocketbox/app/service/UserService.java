package store.pocketbox.app.service;

import store.pocketbox.app.domain.User;

public interface UserService {
    User getUserByEmail(String email);
    Long getUserIdInHeader();
    //"userId" 헤더로 user 뽑아오기
    User getCurrentUser();

    User getLoginUser();

    User getUserById(Long id);

    void deleteUser();

    User updateUser(User user);

    String verifyLogin(User user);

    User registerUser(User user);
}
