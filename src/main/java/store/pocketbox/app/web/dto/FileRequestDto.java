package store.pocketbox.app.web.dto;

import lombok.*;

import java.util.List;

public class FileRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetFileRequest{
        private String name;
        private List<String> path;
        private String filename;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeleteFileRequest {
        private String name;
        private List<String> path;
        private String filename;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateFileRequest {
        private String name;
        private List<String> path;
        private String filename;
    }
}
