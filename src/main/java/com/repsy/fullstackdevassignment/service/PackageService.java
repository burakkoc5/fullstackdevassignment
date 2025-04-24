package com.repsy.fullstackdevassignment.service;

import com.repsy.fullstackdevassignment.entity.PackageEntity;
import com.repsy.fullstackdevassignment.repository.PackageRepository;
import com.repsy.fullstackdevassignment.strategy.StorageStrategy;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final StorageStrategy storageStrategy;

    public void deployPackage(String name, String version, MultipartFile packageFile, MultipartFile metaFile) {
        if (!packageFile.getOriginalFilename().endsWith(".rep") || !metaFile.getOriginalFilename().endsWith(".json")) {
            throw new IllegalArgumentException("Invalid file extensions.");
        }

        try {
            String metaContent = new String(metaFile.getBytes(), StandardCharsets.UTF_8);
            JSONObject meta = new JSONObject(metaContent);

            if (!meta.getString("name").equals(name) || !meta.getString("version").equals(version)) {
                throw new IllegalArgumentException("Metadata mismatch with path.");
            }

            System.out.println("META CONTENT: " + metaContent);


            // Dosyaları kaydet
            try (InputStream is = packageFile.getInputStream()) {
                storageStrategy.store(name, version, "package.rep", is);
            }
            try (InputStream is = metaFile.getInputStream()) {
                storageStrategy.store(name, version, "meta.json", is);
            }

            // DB kaydı
            PackageEntity entity = PackageEntity.builder()
                    .name(name)
                    .version(version)
                    .author(meta.optString("author", "N/A"))
                    .metaJson(metaContent)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            packageRepository.save(entity);

        } catch (Exception e) {
            throw new RuntimeException("Failed to deploy package: " + e.getMessage());
        }
    }


    public ResponseEntity<Resource> downloadPackage(String name, String version, String fileName) {
        try {
            InputStream file = storageStrategy.load(name, version, fileName);
            System.out.println(file);
            InputStreamResource resource = new InputStreamResource(file);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to download package: " + e.getMessage());
        }
    }
}