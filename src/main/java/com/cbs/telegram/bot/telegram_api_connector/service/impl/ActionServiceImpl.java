package com.cbs.telegram.bot.telegram_api_connector.service.impl;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.repository.ActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;

    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public Action getNextAction(String message, String botName) {
        return actionRepository.getByTextAndBotName(message, botName);
    }

    public Action addChild(Long actionId, String text) {
        Action action = actionRepository.getOne(actionId);
        Action child = new Action();
        child.setText(text);
        child.setBotName(action.getBotName());
        child.setParent(action);
        action.getChildren().add(child);
        action = actionRepository.save(action);
        return action.getChildren().get(action.getChildren().size() - 1);
    }

    public void delete(Long actionId) {
        Action action = actionRepository.getOne(actionId);
        action.getParent().getChildren().remove(action);
        actionRepository.save(action.getParent());
    }
}
