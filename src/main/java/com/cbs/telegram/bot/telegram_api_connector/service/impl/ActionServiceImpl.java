package com.cbs.telegram.bot.telegram_api_connector.service.impl;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.repository.ActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionRepository actionRepository;


    @Override
    public Action getNextAction(String message, String botName) {
        return actionRepository.getByTextAndBotSettingName(message, botName);
    }

    public Action addChild(Action action, String text) {
        Action child = new Action();
        child.setText(text);
        child.setBotSetting(action.getBotSetting());
        child.setParent(action);
        action.getChildren().add(child);
        action = actionRepository.save(action);
        return action.getChildren().get(action.getChildren().size() - 1);
    }

    public void delete(Action entity) {
        entity.getParent().getChildren().remove(entity);
        actionRepository.save(entity.getParent());
    }
}
