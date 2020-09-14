package com.suncloudstorage.dto;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
public class FileDTO {
    private String name;
    private String extension;
    private String bucketName;
    private LocalDateTime lastModified;
    private long size;

    public static FileDTO fromS3Object(S3ObjectSummary objectSummary) {
        return FileDTO.builder()
                .bucketName(objectSummary.getBucketName())
                .name(objectSummary.getKey().substring(0, objectSummary.getKey().lastIndexOf(".")))
                .extension(objectSummary.getKey().substring(objectSummary.getKey().lastIndexOf(".") + 1))
                .lastModified(LocalDateTime.ofInstant(
                        objectSummary.getLastModified().toInstant(), ZoneId.systemDefault()))
                .size(objectSummary.getSize())
                .build();
    }
}
