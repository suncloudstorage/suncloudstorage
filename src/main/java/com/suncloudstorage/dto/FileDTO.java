package com.suncloudstorage.dto;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.suncloudstorage.util.DateUtils;
import com.suncloudstorage.util.FileUtils;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
                .name(FileUtils.getExtension(objectSummary.getKey()))
                .extension(FileUtils.getFileNameWithoutExtension(objectSummary.getKey()))
                .lastModified(DateUtils.dateToLocalDateTime(objectSummary.getLastModified()))
                .size(objectSummary.getSize())
                .build();
    }
}
