package store.pocketbox.app.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.security.jwt.Token;
import store.pocketbox.app.security.jwt.TokenService;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        Token token = tokenService.generateToken(email, "USER");

        Optional<User> userOptional = userRepository.findByEmail(email);

        String url = "";

        //최초 로그인 시 회원가입
        if(userOptional.isEmpty()){
            userRepository.save(User.builder()
                    .email(email)
                    .name(oAuth2User.getAttribute("name"))
                    .build());
            url = makeRedirectUrl("signup", token.getAccessToken());
        } else {
            User user = userOptional.get();
            userRepository.save(user);
            url = makeRedirectUrl("login", token.getAccessToken());
        }

        // access token -> 쿼리 스트링으로 전달
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String path, String token) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000")
                .path(path)
                .queryParam("token", token)
                .build().toUriString();
    }
}
