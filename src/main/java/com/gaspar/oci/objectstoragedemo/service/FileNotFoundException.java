package com.gaspar.oci.objectstoragedemo.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileNotFoundException extends RuntimeException {

    @Getter
    private final String fileName;
}
