package store.pocketbox.app.web.dto;

import lombok.*;
import store.pocketbox.app.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private Long userId;
        private String name;
        private String email;

        public static Info of(User user){
            return Info.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }

        public static List<Info> of(List<User> users){
            return users.stream().map(Info::of).collect(Collectors.toList());
        }
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessToken{
        private String Authorization;

        public static AccessToken of(String Authorization){
            return AccessToken.builder()
                    .Authorization(Authorization)
                    .build();
        }

        public static List<AccessToken> of(List<String> Authorizations){
            return Authorizations.stream().map(AccessToken::of).collect(Collectors.toList());
        }
    }
}