package com.cbs.telegram.bot.telegram.api.connector.service.impl;

import com.cbs.telegram.bot.telegram.api.connector.dto.ActionDto;
import com.cbs.telegram.bot.telegram.api.connector.entity.Action;
import com.cbs.telegram.bot.telegram.api.connector.entity.UserLastAction;
import com.cbs.telegram.bot.telegram.api.connector.exception.NoDataFoundException;
import com.cbs.telegram.bot.telegram.api.connector.repository.ActionRepository;
import com.cbs.telegram.bot.telegram.api.connector.repository.UserLastActionRepository;
import com.cbs.telegram.bot.telegram.api.connector.service.ActionService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;
    private final UserLastActionRepository userLastActionRepository;

    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository,
                             UserLastActionRepository userLastActionRepository) {
        this.actionRepository = actionRepository;
        this.userLastActionRepository = userLastActionRepository;
    }

    @Override
    public ActionDto getAction(Long actionId) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new NoDataFoundException("Can't find action with id="
                        + actionId));
        return ActionDto.parseFromActionEntity(action);
    }

    @Override
    public ActionDto getNextUserAction(Long chatId, String message) {
        UserLastAction userLastAction = userLastActionRepository
                .findById(chatId)
                .orElseGet(this::getDefaultUserLastAction);
        Action action = userLastAction.getAction();
        ActionDto actionDto = ActionDto.parseFromActionEntity(action);
        return actionDto.getChildren()
                .stream()
                .filter(Objects::nonNull)
                .map(ActionDto::getChildren)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .filter(a -> message.equalsIgnoreCase(a.getText()))
                .findAny().orElse(null);
    }

    private UserLastAction getDefaultUserLastAction() {
        Action action = new Action();
        action.setChildren(Collections.emptyList());
        return UserLastAction.builder().action(action).build();
    }

    private UserLastAction getDefaultUserLastAction(Long chatId) {
        return UserLastAction.builder().chartId(chatId).build();
    }

    @Override
    public ActionDto getStartUpAction(Long chatId) {
        Action action = new Action();
        //TODO: do not hardcode it! Make it more elegant
        action.setText("Hello world!");
        action.setId(1L);
        Action savedAction = actionRepository.saveAndFlush(action);
        return ActionDto.parseFromActionEntity(savedAction);
    }

    //TODO: test it
    @Override
    public void updateLastUserAction(Long chatId, Long actionId) {
        UserLastAction previousUserAction = userLastActionRepository.findById(chatId)
                .orElseGet(this::getDefaultUserLastAction);
        Action newLastAction = actionRepository.findById(actionId)
                .orElseThrow(() -> new NoDataFoundException("Can't find action with id="
                        + actionId));
        previousUserAction.setAction(newLastAction);
        actionRepository.save(newLastAction);
        actionRepository.flush();
    }

    @Transactional
    public ActionDto addChild(Long actionId, String text) {
        Action parentAction = actionRepository.getOne(actionId);
        Action child = new Action();
        child.setText(text);
        child.setParent(parentAction);
        parentAction.getChildren().add(child);
        parentAction = actionRepository.saveAndFlush(parentAction);

        Action savedAction = parentAction.getChildren().get(parentAction.getChildren().size() - 1);
        return ActionDto.parseFromActionEntity(savedAction);
    }

    @Override
    public ActionDto updateActionMessage(Long actionId, String text) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new NoDataFoundException("Can't find action with id="
                        + actionId));
        action.setText(text);
        Action savedAction = actionRepository.save(action);
        return ActionDto.parseFromActionEntity(savedAction);
    }

    public void deleteAction(Long actionId) {
        Action action = actionRepository.getOne(actionId);
        action.getParent().getChildren().remove(action);
        actionRepository.save(action.getParent());
    }
}
