package store.pocketbox.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import store.pocketbox.app.converter.CommentConverter;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.repository.CommentRepository;
import store.pocketbox.app.repository.PostRepository;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.service.CommentService;
import store.pocketbox.app.web.dto.CommentRequestDto;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment create(@PathVariable Long postId, CommentRequestDto.CreateCommentDto request){
        CommentConverter commentConverter = new CommentConverter(userRepository, postRepository);
        Comment comment = commentConverter.toComment(postId, request);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NoSuchElementException("Comment not found"));

        commentRepository.delete(comment);
    }

    @Transactional
    @Override
    public void updateComment(Long commentId, CommentRequestDto.UpdateCommentDto request){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        comment.setContent(request.getContent());

        commentRepository.save(comment);
    }
}
