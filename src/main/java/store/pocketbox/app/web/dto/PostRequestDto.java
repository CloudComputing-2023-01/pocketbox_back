package store.pocketbox.app.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class PostRequestDto {

    @Getter
    public static class CreatePostDto{
        private String content;
        private String title;
        private Long userId;
    }

    @Getter @Setter
    public static class UpdatePostDto {
        private String title;
        private String content;
    }

    @Getter
    public static class LikePostDto{
        private Long userId;
    }

    @Getter
    public static class LikedPostsDto{
        private Long userId;
    }

}
