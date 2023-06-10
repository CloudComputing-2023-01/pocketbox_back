package store.pocketbox.app.service;

import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.mapping.PostLike;
import store.pocketbox.app.web.dto.PostRequestDto;
import store.pocketbox.app.web.dto.PostResponseDto;

import java.util.List;

public interface PostService {
    Post create(PostRequestDto.CreatePostDto request);

    List<Post> getAllPosts();

    List<Post> getAllPostsByLikeCount();

    List<Post> getLikedPosts(Long userId);

    PostResponseDto.PostDto getPost(Long postId);

    void deletePost(Long postId);

    void updatePost(Long postId, PostRequestDto.UpdatePostDto request);

    Boolean likePost(Long postId, Long userId);

    List<PostResponseDto.PostListDto> searchPost(String keyword);
}
