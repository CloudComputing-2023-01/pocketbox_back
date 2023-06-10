package store.pocketbox.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.pocketbox.app.converter.PostConverter;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.domain.mapping.PostLike;
import store.pocketbox.app.repository.CommentRepository;
import store.pocketbox.app.repository.PostLikeRepository;
import store.pocketbox.app.repository.PostRepository;
import store.pocketbox.app.repository.UserRepository;
import store.pocketbox.app.service.PostService;
import store.pocketbox.app.web.dto.PostRequestDto;
import store.pocketbox.app.web.dto.PostResponseDto;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostConverter postConverter;

    @Transactional
    @Override
    public Post create(PostRequestDto.CreatePostDto request){
        PostConverter postConverter = new PostConverter(userRepository);
        Post post = postConverter.toPost(request);
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Post> getAllPostsByLikeCount() {
        return postRepository.findAllByOrderByLikeCountDesc();
    }

    @Override
    public List<Post> getLikedPosts(Long userId){ return postRepository.findAllByUserIdOrderByCreatedAtDesc(userId); }

    @Override
    public PostResponseDto.PostDto getPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCreatedAt(postId);
        return PostConverter.toPostDto(post, commentList);
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        commentRepository.deleteByPost(post);

        postRepository.delete(post);
    }

    @Transactional
    @Override
    public void updatePost(Long postId, PostRequestDto.UpdatePostDto request){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        postRepository.save(post);
    }

    @Transactional
    @Override
    public Boolean likePost(Long postId, Long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            postLikeRepository.deleteByPostIdAndUserId(postId, userId);
            postRepository.decreaseLikeCount(postId);
            return false; // Post unlike
        } else {
            postRepository.increaseLikeCount(postId);
            PostLike postLike = PostConverter.toPostLike(post, user);
            postLikeRepository.save(postLike);

            return true; // Post like
        }
    }

    @Override
    public List<PostResponseDto.PostListDto> searchPost(String keyword){
        List<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
        return postConverter.toSearchDtoList(posts);
    }

}
