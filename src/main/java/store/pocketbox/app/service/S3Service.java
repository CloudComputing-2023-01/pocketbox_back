package store.pocketbox.app.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface S3Service {
    public static class FilePath {
        public List<String> pathElements;
        public String canonicalPath;

        public FilePath(List<String> _path) {
            pathElements = _path;
            if(_path.stream().anyMatch((x) -> x.contains("/"))) {
                throw new UnsupportedOperationException("");
            }

            var path = pathElements.stream().reduce((x, y) -> x + '/' + y).orElse("");

            canonicalPath = path;
        }

        public FolderPath getParentFolder() {
            return new FolderPath(pathElements.subList(0, pathElements.size() - 2));
        }
    }

    public static class FolderPath {
        public List<String> pathElements;
        public String canonicalPath;

        public FolderPath(List<String> _path) {
            pathElements = _path;
            if(_path.stream().anyMatch((x) -> x.contains("/"))) {
                throw new UnsupportedOperationException("");
            }

            var path = pathElements.stream().reduce((x, y) -> x + '/' + y).orElse("");

            if(!path.endsWith("/")) {
                path += "/";
            }

            canonicalPath = path;
        }
    }

    public static class ListResult {
        public List<FolderPath> directories;
        public List<FilePath> files;

        public ListResult(List<FolderPath> _directories, List<FilePath> _files) {
            directories = _directories;
            files = _files;
        }
    }

    String createPreSignedForUploadFile(FilePath path);

    String createPreSignedForDownloadFile(FilePath path);

    void deleteFile(FilePath path);

    void createFolder(FolderPath path);

    ListResult listFolder(FolderPath path);

    void deleteFolder(FolderPath path);
}
