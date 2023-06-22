package com.gaspar.oci.objectstoragedemo.config;

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OciObjectStorageConfig {

    private final AuthenticationDetailsProvider authenticationDetailsProvider;
    private final ClientConfiguration clientConfiguration;

    @Bean
    public ObjectStorageClient objectStorageClient() {
        return ObjectStorageClient.builder()
                .configuration(clientConfiguration)
                .region(Region.EU_FRANKFURT_1)
                .build(authenticationDetailsProvider);
    }

}
