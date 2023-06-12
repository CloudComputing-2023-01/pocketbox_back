package store.pocketbox.app.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.domain.mapping.PostLike;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.web.dto.CommentResponseDto;
import store.pocketbox.app.web.dto.PostRequestDto;
import store.pocketbox.app.web.dto.PostResponseDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final UserRepository userRepository;

    public static PostResponseDto.CreatePostDto toCreatePostDto(Post post) {
        return PostResponseDto.CreatePostDto.builder()
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public Post toPost(PostRequestDto.CreatePostDto request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .likeCount(0)
                .build();
    }

    public PostResponseDto.PostListDto toPostBoardDto(Post post) {
        return PostResponseDto.PostListDto.builder()
                .postId(post.getId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public List<PostResponseDto.PostListDto> toPostDtoList(List<Post> posts) {
        return posts.stream()
                .map(this::toPostBoardDto)
                .collect(Collectors.toList());
    }


    public static PostResponseDto.PostDto toPostDto(Post post, List<Comment> commentList) {
        List<CommentResponseDto.CommentDto> commentDtoList = commentList.stream()
                .map(comment -> CommentResponseDto.CommentDto.builder()
                        .commentId(comment.getId())
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return PostResponseDto.PostDto.builder()
                .postId(post.getId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .commentDtoList(commentDtoList)
                .build();
    }

    public static PostLike toPostLike(Post post, User user) {
        return PostLike.builder()
                .user(user)
                .post(post)
                .build();
    }

    public List<PostResponseDto.PostListDto> toSearchDtoList(List<Post> posts) {
        return posts.stream()
                .map(this::toPostBoardDto)
                .collect(Collectors.toList());
    }

}