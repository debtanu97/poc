package com.example.api;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.model.FileInfo;
import com.example.service.AwsS3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class S3Controller {

    private final AwsS3Service service;

    private final ObjectMapper objectMapper;

    @PostMapping
    ResponseEntity<?> uploadFile(
            @RequestPart(value = "file") MultipartFile file
    ) {
        try {
            FileInfo fileInfo = service.uploadObjectToS3(file.getOriginalFilename(), file.getBytes());
            return ResponseEntity.ok(fileInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create/bucket")
    ResponseEntity<?> createBucket() {
        service.createBucket();
        return ResponseEntity.ok("Bucket created successfully");
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        S3ObjectInputStream inputStream = service.downloadFileFromS3Bucket(fileName);
        try {
            return ResponseEntity.ok(objectMapper.readValue(inputStream, Map.class));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error occurred while downloading the file");
        }
    }
}
