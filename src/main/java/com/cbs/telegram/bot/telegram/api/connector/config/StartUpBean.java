package com.cbs.telegram.bot.telegram.api.connector.config;

import com.cbs.telegram.bot.telegram.api.connector.updatehandlers.ZayetsHandler;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import org.telegram.telegrambots.meta.logging.BotsFileHandler;

@Component
public class StartUpBean {

    private final BuildConfig buildConfig;
    private final ZayetsHandler zayetsHandler;

    @Autowired
    public StartUpBean(BuildConfig buildConfig, ZayetsHandler zayetsHandler) {
        this.buildConfig = buildConfig;
        this.zayetsHandler = zayetsHandler;
    }

    @PostConstruct
    public void setup() {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        String logTag = "";
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(logTag, e);
        }

        try {
            TelegramBotsApi telegramBotsApi = createTelegramBotsApi();
            try {
                // Register long polling bots.
                // They work regardless type of TelegramBotsApi we are creating
                telegramBotsApi.registerBot(zayetsHandler);
            } catch (TelegramApiException e) {
                BotLogger.error(logTag, e);
            }
        } catch (Exception e) {
            BotLogger.error(logTag, e);
        }
    }

    private TelegramBotsApi createTelegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi;
        if (!buildConfig.getUseWebHook()) {
            // Default (long polling only)
            telegramBotsApi = createLongPollingTelegramBotsApi();
        } else if (!buildConfig.getPathToCertificatePublicKey().isEmpty()) {
            // Filled a path to a pem file ? looks like you're going
            // for the self signed option then, invoke with store and pem file to supply.
            telegramBotsApi = createSelfSignedTelegramBotsApi();
            telegramBotsApi.registerBot(zayetsHandler);
        } else {
            // Non self signed, make sure you've added private/public and if needed
            // intermediate to your cert-store.
            telegramBotsApi = createNoSelfSignedTelegramBotsApi();
            telegramBotsApi.registerBot(zayetsHandler);
        }
        return telegramBotsApi;
    }

    /**
     * Creates a Telegram Bots Api to use Long Polling (getUpdates) bots.
     * @return TelegramBotsApi to register the bots.
     */
    private TelegramBotsApi createLongPollingTelegramBotsApi() {
        return new TelegramBotsApi();
    }

    /**
     * Creates a Telegram Bots Api to use Long Polling bots
     * And webhooks bot with self-signed certificates.
     * @return TelegramBotsApi to register the bots.
     *
     * @note https://core.telegram.org/bots/self-signed#java-keystore for generating a keypair
     * @note Don't forget to split the pem bundle (begin/end), use only the public key as input!
     */
    private TelegramBotsApi createSelfSignedTelegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(buildConfig.getPathToCertificateStore(),
                buildConfig.getCertificateStorePassword(),
                buildConfig.getExternalWebhookUrl(),
                buildConfig.getInternalWebhookUrl(),
                buildConfig.getPathToCertificatePublicKey());
    }

    /**
     * Creates a Telegram Bots Api to use Long Polling bots and
     * webhooks bots with no-self-signed certificates.
     * @return TelegramBotsApi to register the bots.
     *
     * @note Coming from a set of pem files here's one way to do it:
     * <pre>
     * {@code
     * openssl pkcs12 -export -in public.pem -inkey private.pem > keypair.p12
     * keytool -importkeystore -srckeystore keypair.p12 -destkeystore server.jks
     *      -srcstoretype pkcs12
     * #have (an) intermediate(s) to supply? first:
     * cat public.pem intermediate.pem > set.pem (use set.pem as -in)
     * }
     * </pre>
     */
    private TelegramBotsApi createNoSelfSignedTelegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(buildConfig.getPathToCertificateStore(),
                buildConfig.getCertificateStorePassword(),
                buildConfig.getExternalWebhookUrl(),
                buildConfig.getInternalWebhookUrl());
    }
}
