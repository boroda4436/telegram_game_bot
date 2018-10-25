package com.cbs.telegram.bot.telegram_api_connector.service.impl;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.entity.UserLastAction;
import com.cbs.telegram.bot.telegram_api_connector.repository.ActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.repository.UserLastActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        UserLastAction userLastAction = userLastActionRepository.
                findById(chatId).
                orElseGet(this::getDefaultUserLastAction);
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

    private UserLastAction getDefaultUserLastAction() {
        Action action = new Action();
        action.setChildren(Collections.emptyList());
        return UserLastAction.builder().action(action).build();
    }

    @Override
    public Action getStartUpAction(Long chatId) {
        Action action = new Action();
        //TODO: do not hardcode it! Make it more elegant
        action.setText("Hello world!");
        action.setId(1L);
        return actionRepository.saveAndFlush(action);
    }

    //TODO: test it
    @Override
    public void updateLastUserAction(Long chatId, Long actionId) {
        UserLastAction previousUserAction = userLastActionRepository.findById(chatId).orElseGet(this::getDefaultUserLastAction);
        Action newLastAction = actionRepository.getOne(actionId);
        previousUserAction.setAction(newLastAction);
        actionRepository.save(newLastAction);
        actionRepository.flush();
    }

    private UserLastAction getDefaultUserLastAction (Long chatId) {
        return UserLastAction.builder().chartId(chatId).build();
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
