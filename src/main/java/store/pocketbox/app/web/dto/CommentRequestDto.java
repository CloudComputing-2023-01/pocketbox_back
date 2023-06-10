package store.pocketbox.app.web.dto;

import lombok.Getter;
import lombok.Setter;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;

public class CommentRequestDto {

    @Getter
    public static class CreateCommentDto{
        private String content;
        private Long userId;
    }

    @Getter @Setter
    public static class UpdateCommentDto {
        private String content;
    }
}
