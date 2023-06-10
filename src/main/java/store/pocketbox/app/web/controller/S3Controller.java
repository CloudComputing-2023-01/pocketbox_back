package store.pocketbox.app.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.pocketbox.app.converter.S3Converter;
import store.pocketbox.app.exception.StatusCode;
import store.pocketbox.app.service.S3Service;
import store.pocketbox.app.web.dto.FileRequestDto;
import store.pocketbox.app.web.dto.FolderRequestDto;
import store.pocketbox.app.web.dto.base.DefaultRes;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    @Autowired
    private final S3Service s3;

    @GetMapping("/folder")
    public ResponseEntity getChildFolder(@RequestBody FolderRequestDto.GetFolderChildRequest request) {
        try{
            var list = s3.listFolder(S3Converter.toPath(request.getName(), request.getPath()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "", S3Converter.toGetFolderChildResponse(list)), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/folder")
    public ResponseEntity createFolder(@RequestBody FolderRequestDto.CreateFolderRequest request) {
        try {
            s3.createFolder(S3Converter.toPath(request.getName(), request.getPath()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/folder")
    public ResponseEntity deleteFolder(@RequestBody FolderRequestDto.DeleteFolderRequest request) {
        try {
            s3.deleteFolder(S3Converter.toPath(request.getName(), request.getPath()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/file")
    public ResponseEntity createFile(@RequestBody FileRequestDto.CreateFileRequest request) {
        try {
            var res = s3.createPreSignedForUploadFile(S3Converter.toFilePath(request.getName(), request.getPath(), request.getFilename()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "", S3Converter.toCreateFileResponse(res)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestBody FileRequestDto.GetFileRequest request) {
        try {
            var res = s3.createPreSignedForDownloadFile(S3Converter.toFilePath(request.getName(), request.getPath(), request.getFilename()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "", S3Converter.toGetFileResponse(res)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestBody FileRequestDto.DeleteFileRequest request) {
        try {
            s3.deleteFile(S3Converter.toFilePath(request.getName(), request.getPath(), request.getFilename()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
