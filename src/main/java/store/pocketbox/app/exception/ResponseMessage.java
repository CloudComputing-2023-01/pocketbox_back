package store.pocketbox.app.exception;

public class ResponseMessage {

    //Post
    public static final String BOARD_READ_SUCCESS = "게시판 불러오기 성공";
    public static final String BOARD_READ_ORDER_BY_LIKE_SUCCESS = "게시판 좋아요순 불러오기 성공";
    public static final String LIKED_BOARD_READ_SUCCESS = "좋아요 누른 게시물 목록 불러오기 성공";
    public static final String POST_READ_SUCCESS = "게시물 불러오기 성공";
    public static final String POST_POST_SUCCESS = "게시물 작성 성공";
    public static final String POST_UPDATE_SUCCESS = "게시물 수정 성공";
    public static final String POST_DELETE_SUCCESS = "게시물 삭제 성공";
    public static final String POST_LIKE_SUCCESS = "게시물 좋아요";
    public static final String POST_UNLIKE_SUCCESS = "게시물 좋아요 취소";
    public static final String POST_SEARCH_SUCCESS = "게시물 검색 성공";

    //Comment
    public static final String COMMENT_POST_SUCCESS = "댓글 작성 성공";
    public static final String COMMENT_UPDATE_SUCCESS = "댓글 수정 성공";
    public static final String COMMENT_DELETE_SUCCESS = "댓글 삭제 성공";
}
