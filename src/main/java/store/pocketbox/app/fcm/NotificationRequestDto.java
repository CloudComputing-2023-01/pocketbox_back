package store.pocketbox.app.fcm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class NotificationRequestDto {
    private String title;
    private String token;
    private String message;
}
