package com.example.litelog.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class OssService {

    private static final Logger log = LoggerFactory.getLogger(OssService.class);

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.access-key-id}")
    private String accessKeyId;

    @Value("${oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${oss.url-prefix}")
    private String urlPrefix;

    private OSS ossClient;

    @PostConstruct
    public void init() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        log.info("OSS client initialized: endpoint={}, bucket={}", endpoint, bucket);
    }

    @PreDestroy
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    public String uploadFile(String objectKey, byte[] data, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.length);
        if (contentType != null) {
            metadata.setContentType(contentType);
        }

        ossClient.putObject(bucket, objectKey, new ByteArrayInputStream(data), metadata);
        String url = urlPrefix + "/" + objectKey;
        log.info("File uploaded to OSS: {}", url);
        return url;
    }

    public String uploadFile(String objectKey, InputStream inputStream, String contentType, long contentLength) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        if (contentType != null) {
            metadata.setContentType(contentType);
        }

        ossClient.putObject(bucket, objectKey, inputStream, metadata);
        String url = urlPrefix + "/" + objectKey;
        log.info("File uploaded to OSS: {}", url);
        return url;
    }

    public void deleteFile(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }

        String objectKey = extractObjectKey(url);
        if (objectKey == null) {
            return;
        }

        try {
            if (ossClient.doesObjectExist(bucket, objectKey)) {
                ossClient.deleteObject(bucket, objectKey);
                log.info("File deleted from OSS: {}", objectKey);
            }
        } catch (Exception e) {
            log.warn("Failed to delete OSS file: {}", e.getMessage());
        }
    }

    private String extractObjectKey(String url) {
        if (url == null || !url.startsWith(urlPrefix)) {
            return null;
        }
        int prefixLen = urlPrefix.length();
        if (url.length() <= prefixLen) {
            return null;
        }
        return url.substring(prefixLen + 1);
    }
}
