package com.cbs.telegram.bot.telegram_api_connector.service.impl;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.entity.UserLastAction;
import com.cbs.telegram.bot.telegram_api_connector.repository.ActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.repository.UserLastActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;
    private final UserLastActionRepository userLastActionRepository;

    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository, UserLastActionRepository userLastActionRepository) {
        this.actionRepository = actionRepository;
        this.userLastActionRepository = userLastActionRepository;
    }

    @Override
    public Action getAction(Long actionId) {
        return actionRepository.getOne(actionId);
    }

    @Override
    public Action getNextUserAction(Long chatId, String message) {
        UserLastAction userLastAction = userLastActionRepository.getOne(chatId);
        return userLastAction.getAction().getChildren().
                stream().
                filter(Objects::nonNull).
                map(Action::getChildren).
                filter(Objects::nonNull).
                flatMap(List::stream).
                filter(Objects::nonNull).
                filter(action -> message.equalsIgnoreCase(action.getText())).
                findAny().orElse(null);
    }

    //TODO: test it
    @Override
    public void updateLastUserAction(Long chatId, Long actionId) {
        UserLastAction previousUserAction = userLastActionRepository.getOne(chatId);
        Action newLastAction = actionRepository.getOne(actionId);
        previousUserAction.setAction(newLastAction);
        actionRepository.save(newLastAction);
        actionRepository.flush();
    }

    public Action addChild(Long actionId, String text) {
        Action action = actionRepository.getOne(actionId);
        Action child = new Action();
        child.setText(text);
        child.setParent(action);
        action.getChildren().add(child);
        action = actionRepository.save(action);
        return action.getChildren().get(action.getChildren().size() - 1);
    }

    public void deleteAction(Long actionId) {
        Action action = actionRepository.getOne(actionId);
        action.getParent().getChildren().remove(action);
        actionRepository.save(action.getParent());
    }
}
