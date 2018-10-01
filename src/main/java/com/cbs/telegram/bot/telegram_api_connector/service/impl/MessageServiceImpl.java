package com.cbs.telegram.bot.telegram_api_connector.service.impl;

import com.cbs.telegram.bot.telegram_api_connector.entity.Message;
import com.cbs.telegram.bot.telegram_api_connector.repository.MessageRepository;
import com.cbs.telegram.bot.telegram_api_connector.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;


    @Override
    public Message getNextMessage(String message, String botName) {
        return messageRepository.getByTextAndBotSettingName(message, botName);
    }

    public Message addChild(Message message, String text) {
        Message child = new Message();
        child.setText(text);
        child.setBotSetting(message.getBotSetting());
        child.setParent(message);
        message.getChildren().add(child);
        message = messageRepository.save(message);
        return message.getChildren().get(message.getChildren().size() - 1);
    }

    public void delete(Message entity) {
        entity.getParent().getChildren().remove(entity);
        messageRepository.save(entity.getParent());
    }
}
