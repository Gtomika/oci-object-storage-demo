package com.gaspar.oci.objectstoragedemo.service;

import com.gaspar.oci.objectstoragedemo.dto.DownloadFileResponse;
import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final ObjectStorageClient objectStorageClient;

    @Value("${infrastructure.oci.object-storage.namespace}")
    private String namespace;

    @Value("${infrastructure.oci.object-storage.bucket}")
    private String bucketName;

    /**
     * Uploads the file to the selected OCI bucket.
     * @return The key of the object (same as file name for now).
     */
    public String uploadFile(String fileName, InputStream content) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .objectName(fileName)
                .putObjectBody(content)
                .build();
        objectStorageClient.putObject(putRequest);
        log.info("File '{}' was uploaded successfully to OCI bucket '{}'", fileName, bucketName);
        return fileName;
    }

    /**
     * Downloads the file with the given key.
     * @return Input stream to the downloaded file.
     */
    public DownloadFileResponse downloadFile(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .objectName(key)
                .build();
        try {
            GetObjectResponse getResponse = objectStorageClient.getObject(getRequest);
            log.info("Successfully downloaded file '{}' from OCI bucket '{}'", key, bucketName);
            return new DownloadFileResponse(
                    getResponse.getInputStream(),
                    getResponse.getContentLength()
            );
        } catch (BmcException e) {
            checkAndPropagateOciException(key, e);
            return null;
        }
    }

    private void checkAndPropagateOciException(String key, BmcException e) {
        if(e.getStatusCode() == 404) {
            //technically it could be because bucket not found as well
            log.info("File with name '{}' was not found in the OCI bucket '{}'", key, bucketName);
            throw new FileNotFoundException(key);
        } else {
            log.error("Unexpected error while getting file '{}' from OCI bucket '{}'", key, bucketName);
            throw e;
        }
    }

}
