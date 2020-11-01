package com.suncloudstorage.service;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class EncryptService {

    @Value("${encryptor.url}")
    private String encryptorUrl;

    private static final ParameterizedTypeReference<HttpEntity<byte[]>> HTTP_ENTITY_PARAMETERIZED_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestTemplate restTemplate = new RestTemplate();

    public byte[] encryptFile(MultipartFile file) throws IOException {
        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes());

        ResponseEntity<HttpEntity<byte[]>> encryptedFile =
                restTemplate.exchange(encryptorUrl + "/encrypt", HttpMethod.POST, entity, HTTP_ENTITY_PARAMETERIZED_TYPE_REFERENCE);

        return getBytesFromHttpEntity(encryptedFile);
    }

    public byte[] decryptFile(S3Object fileFroms3bucket) throws IOException {
        byte[] bytes = fileFroms3bucket.getObjectContent().readAllBytes();

        ResponseEntity<HttpEntity<byte[]>> decryptedFile = restTemplate.exchange(encryptorUrl + "/decrypt",
                        HttpMethod.POST,
                        new HttpEntity<>(bytes),
                        HTTP_ENTITY_PARAMETERIZED_TYPE_REFERENCE);

        return getBytesFromHttpEntity(decryptedFile);
    }

    private byte[] getBytesFromHttpEntity(ResponseEntity<HttpEntity<byte[]>> exchange) {
        return Optional.of(exchange)
                .map(HttpEntity::getBody)
                .map(HttpEntity::getBody)
                .orElse(new byte[]{});
    }
}
