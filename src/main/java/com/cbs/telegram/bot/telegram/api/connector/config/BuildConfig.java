package com.cbs.telegram.bot.telegram.api.connector.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Log4j2
@Component
@ConfigurationProperties(prefix = "bot.config")
public class BuildConfig {
    private String username;
    private String token;
    private Boolean useWebHook;
    private String externalWebhookUrl;
    private String internalWebhookUrl;
    private String pathToCertificatePublicKey;
    private String pathToCertificateStore;
    private String certificateStorePassword;
}
