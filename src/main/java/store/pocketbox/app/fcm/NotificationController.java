package store.pocketbox.app.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.pocketbox.app.repository.UserRepository;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String token) {
        notificationService.register(token);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/test/{fbToken}")
    public void send(@PathVariable String fbToken) throws ExecutionException, InterruptedException {
        NotificationRequest notificationRequest = NotificationRequest.builder().title("test").message("tmg").token(fbToken).build();
        notificationService.sendNotification(notificationRequest);
    }

}
