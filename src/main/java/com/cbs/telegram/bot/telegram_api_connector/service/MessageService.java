package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.entity.Message;

public interface MessageService {
    Message getNextMessage(String botName, String message);
}
