package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.dto.ActionDto;

public interface ActionService {
    ActionDto getAction(Long actionId);
    ActionDto addChild(Long actionId, String text);
    ActionDto updateActionMessage(Long actionId, String text);
    void deleteAction(Long actionId);
    ActionDto getNextUserAction(Long chatId, String message);
    ActionDto getStartUpAction(Long chatId);
    void updateLastUserAction(Long chatId, Long actionId);
}
