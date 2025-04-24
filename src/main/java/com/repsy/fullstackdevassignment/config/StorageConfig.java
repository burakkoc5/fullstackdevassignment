package com.repsy.fullstackdevassignment.config;


import com.repsy.fullstackdevassignment.strategy.StorageStrategy;
import com.repsy.storage.filesystem.FileSystemStorage;
import com.repsy.storage.object.ObjectStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${storage.strategy}")
    private String strategy;

    @Value("${storage.base-path:files}")
    private String basePath;

    @Value("${minio.endpoint:}")
    private String minioEndpoint;

    @Value("${minio.access-key:}")
    private String minioAccessKey;

    @Value("${minio.secret-key:}")
    private String minioSecretKey;

    @Value("${minio.bucket-name:packages}")
    private String minioBucketName;

    @Bean
    public StorageStrategy storageStrategy() {
        if (strategy.equals("object-storage")) {
            return new ObjectStorage(minioEndpoint, minioAccessKey, minioSecretKey, minioBucketName);
        } else {
            return new FileSystemStorage(basePath);
        }
    }
}
