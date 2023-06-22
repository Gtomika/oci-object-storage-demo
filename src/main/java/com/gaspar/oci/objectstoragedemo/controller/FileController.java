package com.gaspar.oci.objectstoragedemo.controller;

import com.gaspar.oci.objectstoragedemo.dto.DownloadFileResponse;
import com.gaspar.oci.objectstoragedemo.dto.FileUploadResponse;
import com.gaspar.oci.objectstoragedemo.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final ObjectStorageService objectStorageService;

    @PostMapping
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Received file '{}'. Attempting to open and upload to OCI...", file.getOriginalFilename());
        try(InputStream content = file.getInputStream()) {
            String objectKey = objectStorageService.uploadFile(file.getOriginalFilename(), content);
            return new FileUploadResponse(objectKey);
        }
    }

    @GetMapping(value = "/{key}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String key) throws IOException {
        log.info("Attempting to download the file '{}' from OCI...", key);
        try(DownloadFileResponse downloadedFile = objectStorageService.downloadFile(key)) {
            Resource resource = new ByteArrayResource(IOUtils.toByteArray(downloadedFile.getContent()));
            return ResponseEntity.ok()
                    .headers(buildFileHeaders(key, downloadedFile.getContentLength()))
                    .contentLength(downloadedFile.getContentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
    }

    private HttpHeaders buildFileHeaders(String key, long contentLength) {
        HttpHeaders headers = new HttpHeaders();

        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(key)
                .build();
        headers.setContentDisposition(disposition);

        headers.setContentLength(contentLength);

        return headers;
    }
}
