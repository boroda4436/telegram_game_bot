package com.cbs.telegram.bot.telegram_api_connector.config;

import com.cbs.telegram.bot.telegram_api_connector.updatehandlers.ZayetsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import org.telegram.telegrambots.meta.logging.BotsFileHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

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
        String LOGTAG = "";
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOGTAG, e);
        }

        try {
            TelegramBotsApi telegramBotsApi = createTelegramBotsApi();
            try {
                // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
                telegramBotsApi.registerBot(zayetsHandler);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private TelegramBotsApi createTelegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi;
        if (!buildConfig.getUseWebHook()) {
            // Default (long polling only)
            telegramBotsApi = createLongPollingTelegramBotsApi();
        } else if (!buildConfig.getPathToCertificatePublicKey().isEmpty()) {
            // Filled a path to a pem file ? looks like you're going for the self signed option then, invoke with store and pem file to supply.
            telegramBotsApi = createSelfSignedTelegramBotsApi();
            telegramBotsApi.registerBot(zayetsHandler);
        } else {
            // Non self signed, make sure you've added private/public and if needed intermediate to your cert-store.
            telegramBotsApi = createNoSelfSignedTelegramBotsApi();
            telegramBotsApi.registerBot(zayetsHandler);
        }
        return telegramBotsApi;
    }

    /**
     * @brief Creates a Telegram Bots Api to use Long Polling (getUpdates) bots.
     * @return TelegramBotsApi to register the bots.
     */
    private TelegramBotsApi createLongPollingTelegramBotsApi() {
        return new TelegramBotsApi();
    }

    /**
     * @brief Creates a Telegram Bots Api to use Long Polling bots and webhooks bots with self-signed certificates.
     * @return TelegramBotsApi to register the bots.
     *
     * @note https://core.telegram.org/bots/self-signed#java-keystore for generating a keypair in store and exporting the pem.
     *  @note Don't forget to split the pem bundle (begin/end), use only the public key as input!
     */
    private TelegramBotsApi createSelfSignedTelegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(buildConfig.getPathToCertificateStore(),
                buildConfig.getCertificateStorePassword(),
                buildConfig.getExternalWebhookUrl(),
                buildConfig.getInternalWebhookUrl(),
                buildConfig.getPathToCertificatePublicKey());
    }

    /**
     * @brief Creates a Telegram Bots Api to use Long Polling bots and webhooks bots with no-self-signed certificates.
     * @return TelegramBotsApi to register the bots.
     *
     * @note Coming from a set of pem files here's one way to do it:
     * @code{.sh}
     * openssl pkcs12 -export -in public.pem -inkey private.pem > keypair.p12
     * keytool -importkeystore -srckeystore keypair.p12 -destkeystore server.jks -srcstoretype pkcs12
     * #have (an) intermediate(s) to supply? first:
     * cat public.pem intermediate.pem > set.pem (use set.pem as -in)
     * @endcode
     */
    private TelegramBotsApi createNoSelfSignedTelegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(buildConfig.getPathToCertificateStore(),
                buildConfig.getCertificateStorePassword(),
                buildConfig.getExternalWebhookUrl(),
                buildConfig.getInternalWebhookUrl());
    }
}
