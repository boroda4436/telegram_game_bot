package com.cbs.telegram.bot.telegram_api_connector.service;

public interface BotSettingService {
    void createBot(String botName, String botToken, String welcomeMsg);
}
