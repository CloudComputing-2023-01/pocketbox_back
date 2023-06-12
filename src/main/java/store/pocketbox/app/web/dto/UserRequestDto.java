package store.pocketbox.app.web.dto;

import lombok.*;
import store.pocketbox.app.domain.User;


public class UserRequestDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register{
        private String name;
        private String email;
        private String password;

        public User toEntity(){
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .password(this.password)
                    .build();
        }
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login{
        private String email;
        private String password;
        public User toEntity(){
            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .build();
        }
    }
}
