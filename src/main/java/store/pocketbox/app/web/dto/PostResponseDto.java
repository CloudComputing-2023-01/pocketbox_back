package store.pocketbox.app.web.dto;

import lombok.*;
import store.pocketbox.app.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostDto{
        private Long postId;
        private String nickname;
        private String title;
        private String content;
        private Integer likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CommentResponseDto.CommentDto> commentDtoList;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreatePostDto{
        private Long postId;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostListDto{
        private Long postId;
        private String nickname;
        private String title;
        private String content;
        private Integer likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
