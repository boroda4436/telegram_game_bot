package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;

public interface ActionService {
    Action get(Long actionId);
    Action getNextUserAction(Long chatId, String message);
    Action addChild(Long actionId, String text);
    void delete(Long actionId);
}
