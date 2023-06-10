package store.pocketbox.app.converter;

import store.pocketbox.app.service.S3Service;
import store.pocketbox.app.web.dto.FileResponseDto;
import store.pocketbox.app.web.dto.FolderResponseDto;

import java.util.ArrayList;
import java.util.List;

public class S3Converter {
    public static FolderResponseDto.GetFolderChildResponse toGetFolderChildResponse(List<S3Service.FolderPath> folderPath, List<S3Service.FilePath> filePaths) {
        var list = new ArrayList<String>();

        list.addAll(folderPath.stream().map((x) -> x.canonicalPath).toList());
        list.addAll(filePaths.stream().map((x) -> x.canonicalPath).toList());

        return FolderResponseDto.GetFolderChildResponse.builder()
                .name(list).build();
    }

    public static FileResponseDto.GetFileResponse toGetFileResponse(String presigned) {
        return FileResponseDto.GetFileResponse.builder().preSigned(presigned).build();
    }

    public static FileResponseDto.CreateFileResponse toCreateFileResponse(String presigned) {
        return FileResponseDto.CreateFileResponse.builder().preSigned(presigned).build();
    }

    public static S3Service.FolderPath toPath(String username, List<String> path) {


        var list = new ArrayList<String>();
        list.add(username);
        list.addAll(path);
        return new S3Service.FolderPath(list);
    }

    public static S3Service.FilePath toFilePath(String username, List<String> path, String filename) {
        if(username.contains("/") || path.stream().anyMatch((x) -> x.contains("/"))) {
            throw new UnsupportedOperationException("");
        }

        var list = new ArrayList<String>();
        list.add(username);
        list.addAll(path);
        list.add(filename);

        return new S3Service.FilePath(list);
    }
}
