package com.suncloudstorage.rest;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.suncloudstorage.dto.FileDTO;
import com.suncloudstorage.service.AmazonS3Service;
import com.suncloudstorage.service.EncryptService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/storage")
public class BucketController {

    private final AmazonS3Service amazonS3Service;

    private final EncryptService encryptService;

    BucketController(AmazonS3Service amazonS3Service, EncryptService encryptService) {
        this.amazonS3Service = amazonS3Service;
        this.encryptService = encryptService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file, Principal principal) throws IOException {
//        amazonS3Service.createBucketIfNotExist(principal.getName());
        byte[] bytes = encryptService.encryptFile(file);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        String url = this.amazonS3Service.uploadFileTos3bucket(principal.getName(), file.getOriginalFilename(), bytes, objectMetadata);
        return ResponseEntity.ok().body(url);
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<?> deleteFile(@RequestPart(value = "url") String fileUrl, Principal principal) {
        this.amazonS3Service.deleteFileFromS3Bucket(principal.getName(), fileUrl);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteFiles")
    public ResponseEntity<?> deleteAllFiles(Principal principal) {
        this.amazonS3Service.deleteAllFilesFromS3Bucket(principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam(value = "url") String fileName, Principal principal) throws IOException {
        S3Object fileFroms3bucket = this.amazonS3Service.downloadFileFroms3bucket(principal.getName(), fileName);
        byte[] decryptedFile = encryptService.decryptFile(fileFroms3bucket);

        String contentType = fileFroms3bucket.getObjectMetadata().getContentType();


        ByteArrayResource resource = new ByteArrayResource(decryptedFile);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Content-Disposition", "attachment; filename=" + fileFroms3bucket.getKey())
                .body(resource);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileDTO>> getFilesByBucket(Principal principal) {
        ObjectListing allFiles = this.amazonS3Service.getAllFiles(principal.getName());
        List<FileDTO> files = allFiles.getObjectSummaries().stream()
                .map(FileDTO::fromS3Object)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(files);
    }
}
