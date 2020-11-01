package com.suncloudstorage.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.costandusagereport.model.AWSRegion;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.suncloudstorage.dto.EditFileDTO;
import com.suncloudstorage.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;

import static com.suncloudstorage.constant.S3Constants.URL_PATTERN;

@Service
public class AmazonS3Service {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(AWSRegion.UsWest2.toString())
                .build();
    }

    public S3Object downloadFileFromS3Bucket(String bucketName, String fileUrl) {
        String fileName = FileUtils.getFileNameFromUrl(fileUrl);
        return s3client.getObject(new GetObjectRequest(bucketName, fileName));
    }

    public String uploadFileToS3Bucket(String bucketName, String fileName, byte[] file, ObjectMetadata objectMetadata) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(file), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return String.format(URL_PATTERN, endpointUrl, bucketName, fileName);
    }

    public void deleteFileFromS3Bucket(String bucketName, String fileUrl) {
        String fileName = FileUtils.getFileNameFromUrl(fileUrl);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    public void deleteAllFilesFromS3Bucket(String bucketName) {
        ObjectListing objectListing = s3client.listObjects(bucketName);
        objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .forEach(fileName -> s3client.deleteObject(bucketName, fileName));
    }

    public ObjectListing getAllFiles(String bucketName) {
        return s3client.listObjects(bucketName);
    }

    public CopyObjectResult editFileName(EditFileDTO editFileDTO, String bucketName) {
        CopyObjectResult copyObjectResult = s3client.copyObject(bucketName, editFileDTO.getOldName(), bucketName, editFileDTO.getNewName());
        s3client.deleteObject(bucketName, editFileDTO.getOldName());

        return copyObjectResult;
    }
}
