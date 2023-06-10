package store.pocketbox.app.web.dto;

import lombok.*;

import java.util.List;

public class FolderResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GetFolderChildResponse {
        private List<String> name;
    }
}
