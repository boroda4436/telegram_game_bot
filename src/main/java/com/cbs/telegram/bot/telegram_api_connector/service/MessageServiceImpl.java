package com.cbs.telegram.bot.telegram_api_connector.service;

import com.cbs.telegram.bot.telegram_api_connector.entity.Message;
import com.cbs.telegram.bot.telegram_api_connector.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;


    @Override
    public Message getNextMessage(String botName, String message) {
        return null;
    }
}
