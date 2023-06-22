package com.gaspar.oci.objectstoragedemo.dto;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

@Data
public class DownloadFileResponse implements AutoCloseable {

    private final InputStream content;
    private final long contentLength;

    @Override
    public void close() throws IOException {
        content.close();
    }
}
