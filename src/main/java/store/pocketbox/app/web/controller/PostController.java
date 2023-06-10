package store.pocketbox.app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.pocketbox.app.converter.PostConverter;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.exception.ResponseMessage;
import store.pocketbox.app.exception.StatusCode;
import store.pocketbox.app.service.PostService;
import store.pocketbox.app.web.dto.PostRequestDto;
import store.pocketbox.app.web.dto.PostResponseDto;
import store.pocketbox.app.web.dto.base.DefaultRes;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostConverter postConverter;

    @GetMapping("/board")
    public ResponseEntity getPostList() {
        try{
            List<Post> posts = postService.getAllPosts();
            List<PostResponseDto.PostListDto> postListDto = postConverter.toPostDtoList(posts);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_READ_SUCCESS, postListDto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/board/order-by-like")
    public ResponseEntity getPostListOrderByLike() {
        try{
            List<Post> posts = postService.getAllPostsByLikeCount();
            List<PostResponseDto.PostListDto> postListDto = postConverter.toPostDtoList(posts);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.BOARD_READ_ORDER_BY_LIKE_SUCCESS, postListDto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/board/like")
    public ResponseEntity getLikedPostList(@RequestBody PostRequestDto.LikedPostsDto request) {
        try{
            List<Post> posts = postService.getLikedPosts(request.getUserId());
            List<PostResponseDto.PostListDto> postListDto = postConverter.toPostDtoList(posts);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.LIKED_BOARD_READ_SUCCESS, postListDto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/board/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId){
        try{
            PostResponseDto.PostDto postDto = postService.getPost(postId);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_READ_SUCCESS, postDto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/board/post")
    public ResponseEntity createPost(@RequestBody PostRequestDto.CreatePostDto request, User user){
        try{
            Post post = postService.create(request);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_POST_SUCCESS, postConverter.toCreatePostDto(post)), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/board/{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId, @RequestBody PostRequestDto.UpdatePostDto request){
        try{
            postService.updatePost(postId, request);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_UPDATE_SUCCESS), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/board/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId){
        try{
            postService.deletePost(postId);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_DELETE_SUCCESS), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/board/{postId}/like")
    public ResponseEntity likePost(@PathVariable Long postId, @RequestBody PostRequestDto.LikePostDto request){
        try{
            Boolean liked = postService.likePost(postId, request.getUserId());
            String responseMessage = liked ? ResponseMessage.POST_LIKE_SUCCESS : ResponseMessage.POST_UNLIKE_SUCCESS;
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, responseMessage), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/board/search")
    public ResponseEntity searchPost(@RequestParam("keyword") String keyword){
        try{
            List<PostResponseDto.PostListDto> postListDto = postService.searchPost(keyword);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.POST_SEARCH_SUCCESS, postListDto), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

}
