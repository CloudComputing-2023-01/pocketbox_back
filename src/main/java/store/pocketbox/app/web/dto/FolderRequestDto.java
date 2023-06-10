package store.pocketbox.app.web.dto;

import lombok.*;

import java.util.List;

public class FolderRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetFolderChildRequest{
        private String name;
        private List<String> path;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateFolderRequest {
        private String name;
        private List<String> path;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeleteFolderRequest {
        private String name;
        private List<String> path;
    }
}
