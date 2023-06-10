package store.pocketbox.app.converter;

import org.springframework.web.bind.annotation.PathVariable;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.repository.PostRepository;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.web.dto.CommentRequestDto;
import store.pocketbox.app.web.dto.CommentResponseDto;

import java.util.NoSuchElementException;

public class CommentConverter {
    private UserRepository userRepository;
    private PostRepository postRepository;

    public CommentConverter(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public static CommentResponseDto.CreateCommentDto toCreateCommentDto(Comment comment){
        return CommentResponseDto.CreateCommentDto.builder()
                .commentId(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public Comment toComment(@PathVariable Long postId, CommentRequestDto.CreateCommentDto request){
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        return Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();
    }

}
