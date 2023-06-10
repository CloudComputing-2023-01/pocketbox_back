package store.pocketbox.app.service;

import org.springframework.web.bind.annotation.PathVariable;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.web.dto.CommentRequestDto;

public interface CommentService {

    Comment create(@PathVariable Long postId, CommentRequestDto.CreateCommentDto request);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, CommentRequestDto.UpdateCommentDto request);
}
