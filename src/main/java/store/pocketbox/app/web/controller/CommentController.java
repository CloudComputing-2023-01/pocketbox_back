package store.pocketbox.app.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.pocketbox.app.converter.CommentConverter;
import store.pocketbox.app.converter.PostConverter;
import store.pocketbox.app.domain.Comment;
import store.pocketbox.app.domain.Post;
import store.pocketbox.app.domain.User;
import store.pocketbox.app.exception.ResponseMessage;
import store.pocketbox.app.exception.StatusCode;
import store.pocketbox.app.service.CommentService;
import store.pocketbox.app.web.dto.CommentRequestDto;
import store.pocketbox.app.web.dto.CommentResponseDto;
import store.pocketbox.app.web.dto.PostRequestDto;
import store.pocketbox.app.web.dto.PostResponseDto;
import store.pocketbox.app.web.dto.base.DefaultRes;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/board/comment/{postId}")
    public ResponseEntity createComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentRequestDto.CreateCommentDto request, User user){
        try{
            Comment comment = commentService.create(postId, request);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.COMMENT_POST_SUCCESS), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/board/comment/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto.UpdateCommentDto request){
        try{
            commentService.updateComment(commentId, request);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.COMMENT_UPDATE_SUCCESS), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/board/comment/{commentId}")
    public ResponseEntity deletePost(@PathVariable Long commentId){
        try{
            commentService.deleteComment(commentId);
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ResponseMessage.COMMENT_DELETE_SUCCESS), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

}
