package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.entity.Message;

public interface MessageService {
    Message getNextMessage(String message, String botName);
    Message addChild(Message message, String text);
    void delete(Message entity);
}
