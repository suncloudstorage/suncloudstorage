package com.suncloudstorage.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.costandusagereport.model.AWSRegion;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;

@Service
public class AmazonS3Service {

    private static final String SEPARATOR = "/";
    private static final String URL_PATTERN = "%s/%s/%s";

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(AWSRegion.UsWest2.toString())
                .build();
    }

    public S3Object downloadFileFroms3bucket(String bucketName, String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(SEPARATOR) + 1);
        return s3client.getObject(new GetObjectRequest(bucketName, fileName));
    }

    public String uploadFileTos3bucket(String bucketName, String fileName, byte[] file, ObjectMetadata objectMetadata) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(file), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return String.format(URL_PATTERN, endpointUrl, bucketName, fileName);
    }

    public void deleteFileFromS3Bucket(String bucketName, String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(SEPARATOR) + 1);
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

}
