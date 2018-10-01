package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;

public interface ActionService {
    Action getNextAction(String message, String botName);
    Action addChild(Action action, String text);
    void delete(Action entity);
}
