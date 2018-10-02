package com.cbs.telegram.bot.telegram_api_connector.updatehandlers;

import com.cbs.telegram.bot.telegram_api_connector.config.BotConfig;
import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.service.ActionService;
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

import java.util.ArrayList;
import java.util.List;

@Component
public class ZayetsHandler extends TelegramLongPollingBot {
    private static final String LOGTAG = "ZAYETS_HANDLER";

    private final ActionService actionService;

    @Autowired
    public ZayetsHandler(ActionService actionService) {
        this.actionService = actionService;
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
        return BotConfig.ZAYETS_USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.ZAYETS_TOKEN;
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(message.getChatId());
        Action nextAction = actionService.getNextAction(BotConfig.ZAYETS_USER, message.getText());
        sendMessageRequest.setReplyMarkup(getPossibleResponseKeyboard(nextAction));
        sendMessageRequest.setText(nextAction.getText());
        execute(sendMessageRequest);
    }

    private static ReplyKeyboardMarkup getPossibleResponseKeyboard(Action action) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        action.getChildren().forEach(m -> {
            KeyboardRow row = new KeyboardRow();
            row.add(m.getText());
            keyboard.add(row);
        });
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
