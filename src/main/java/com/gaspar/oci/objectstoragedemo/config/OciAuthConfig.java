package com.gaspar.oci.objectstoragedemo.config;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Configure the credentials (an API key) that the SDK needs.
 */
@Slf4j
@Configuration
public class OciAuthConfig {

    @Value("${infrastructure.oci.config.file-path}")
    private String ociConfigFilePath;

    @Value("${infrastructure.oci.config.profile-name}")
    private String ociConfigProfileName;

    @Bean
    public AuthenticationDetailsProvider authenticationDetailsProvider() throws IOException {
        log.info("Creating OCI credentials: using file '{}' and profile '{}'", ociConfigFilePath, ociConfigProfileName);
        ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(
                ociConfigFilePath,
                ociConfigProfileName
        );
        return SimpleAuthenticationDetailsProvider.builder()
                .tenantId(configFile.get("tenancy"))
                .userId(configFile.get("user"))
                .fingerprint(configFile.get("fingerprint"))
                .privateKeySupplier(new SimplePrivateKeySupplier(configFile.get("key_file")))
                .build();
    }

}
