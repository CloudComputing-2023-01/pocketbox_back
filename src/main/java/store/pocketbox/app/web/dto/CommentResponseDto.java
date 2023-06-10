package store.pocketbox.app.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class CommentResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentDto{
        private Long commentId;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateCommentDto{
        private Long commentId;
        private LocalDateTime createdAt;
    }

}
