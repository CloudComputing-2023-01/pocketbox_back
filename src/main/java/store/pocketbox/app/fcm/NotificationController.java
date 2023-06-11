package store.pocketbox.app.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.pocketbox.app.exception.ResponseMessage;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.web.dto.base.DefaultRes;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<DefaultRes<NotificationRequestDto>> register(@RequestBody String token) {
        notificationService.register(token);
        return ResponseEntity.ok(DefaultRes.<NotificationRequestDto>builder().statusCode(200).responseMessage(ResponseMessage.COMMENT_DELETE_SUCCESS).data(NotificationRequestDto.builder().title("firebase token").message("register complete").token(token).build()).build());
    }
    @GetMapping("/test/{fbToken}")
    public void send(@PathVariable String fbToken) throws ExecutionException, InterruptedException {
        NotificationRequestDto notificationRequest = NotificationRequestDto.builder().title("test").message("tmg").token(fbToken).build();
        notificationService.sendNotification(notificationRequest);
    }

}
