package store.pocketbox.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.mapping.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsByPostIdAndUserId(Long postId, Long userId);

    void deleteByPostIdAndUserId(Long postId, Long userId);
}
