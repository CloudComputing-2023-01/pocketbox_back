package store.pocketbox.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAt(Long postId);

    void deleteByPost(Post post);
}
