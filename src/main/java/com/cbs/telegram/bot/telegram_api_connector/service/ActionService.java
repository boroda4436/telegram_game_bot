package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;

public interface ActionService {
    Action getAction(Long actionId);
    Action addChild(Long actionId, String text);
    Action updateActionMessage(Long actionId, String text);
    void deleteAction(Long actionId);
    Action getNextUserAction(Long chatId, String message);
    Action getStartUpAction(Long chatId);
    void updateLastUserAction(Long chatId, Long actionId);
}
