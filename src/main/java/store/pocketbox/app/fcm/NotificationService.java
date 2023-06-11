package store.pocketbox.app.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.service.impl.UserServiceImpl;

import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    public void register(final String token) {
        User user = userService.getLoginUser();
        user.setFirebaseToken(token);

        userRepository.save(user);
    }
    public void sendNotification(NotificationRequestDto notificationRequest) throws ExecutionException, InterruptedException {
        Message message = Message.builder()
                .setToken(notificationRequest.getToken())
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                        .setNotification(new WebpushNotification(notificationRequest.getTitle(),
                                notificationRequest.getMessage()))
                        .build())
                .build();
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
    }
}
