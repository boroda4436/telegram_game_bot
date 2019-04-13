package com.cbs.telegram.bot.telegram.api.connector.updatehandlers;

import com.cbs.telegram.bot.telegram.api.connector.config.BuildConfig;
import com.cbs.telegram.bot.telegram.api.connector.dto.ActionDto;
import com.cbs.telegram.bot.telegram.api.connector.service.ActionService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

@Component
public class ZayetsHandler extends TelegramLongPollingBot {
    private static final String LOGTAG = "ZAYETS_HANDLER";

    private final ActionService actionService;
    private final String botUsername;
    private final String botToken;

    @Autowired
    public ZayetsHandler(ActionService actionService, BuildConfig buildConfig) {
        this.actionService = actionService;
        botUsername = buildConfig.getUsername();
        botToken = buildConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                handleIncomingMessage(message);
            }
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    //TODO: verify and implement
    private void handleIncomingMessage(Message message) throws TelegramApiException {
        Long chatId = message.getChatId();
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(chatId);
        ActionDto nextAction = actionService.getNextUserAction(chatId, message.getText());
        if (nextAction == null) {
            nextAction = actionService.getStartUpAction(chatId);
        }

        actionService.updateLastUserAction(chatId, nextAction.getId());
        sendMessageRequest.setReplyMarkup(getPossibleResponseKeyboard(nextAction));
        String text = nextAction.getText();
        sendMessageRequest.setText(text);
        execute(sendMessageRequest);
    }

    private static ReplyKeyboardMarkup getPossibleResponseKeyboard(ActionDto action) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        if (action != null) {
            action.getChildren().forEach(m -> {
                KeyboardRow row = new KeyboardRow();
                row.add(m.getText());
                keyboard.add(row);
            });
        } else {
            KeyboardRow row = new KeyboardRow();
            row.add("Let's start the game!");
            keyboard.add(row);
        }


        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
