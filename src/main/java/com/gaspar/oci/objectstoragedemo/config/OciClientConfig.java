package com.gaspar.oci.objectstoragedemo.config;

import com.oracle.bmc.ClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure SDK client properties: applies to any SDK client such as object storage.
 */
@Slf4j
@Configuration
public class OciClientConfig {

    @Value("${infrastructure.oci.config.connection-timeout-millis}")
    private int connectionTimeoutMillis;

    @Value("${infrastructure.oci.config.read-timeout-millis}")
    private int readTimeoutMillis;

    @Bean
    public ClientConfiguration clientConfiguration() {
        log.info("Creating OCI client config with connection timeout {} and read timeout {}",
                connectionTimeoutMillis, readTimeoutMillis);
        return ClientConfiguration.builder()
                .connectionTimeoutMillis(connectionTimeoutMillis)
                .readTimeoutMillis(readTimeoutMillis)
                .build();
    }

}
