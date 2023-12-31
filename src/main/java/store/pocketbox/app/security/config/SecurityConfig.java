package store.pocketbox.app.security.config;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import store.pocketbox.app.security.OAuth2SuccessHandler;
import store.pocketbox.app.security.UserOAuth2Service;
import store.pocketbox.app.security.jwt.JwtAuthFilter;
import store.pocketbox.app.security.jwt.TokenService;
import store.pocketbox.app.service.UserService;
import store.pocketbox.app.service.impl.UserServiceImpl;

import static java.util.List.of;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final UserOAuth2Service userOAuth2Service;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;
    private final UserServiceImpl userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic().disable()
                .cors().configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOriginPatterns(of("*"));
                    cors.setAllowedMethods(of("GET","POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(of("*"));
                    cors.setAllowCredentials(true);
                    return cors;
                })
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers( "/token/**", "/post/popular","/v2/api-docs",
//                        "/swagger*/**","/oauth2/**","/").permitAll()
//                .anyRequest().authenticated()
                .anyRequest().permitAll()// 임시 설정
                .and()
                .oauth2Login().loginPage("/token/expired")
                .successHandler(successHandler)
                .userInfoEndpoint().userService(userOAuth2Service);

        return http.addFilterBefore(new JwtAuthFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class).build();
    }
}
