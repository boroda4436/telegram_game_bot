package com.cbs.telegram.bot.telegram_api_connector.updatehandlers;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.repository.BotSettingRepository;
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
    private final String botId;
    private final String botToken;
    @Autowired
    public ZayetsHandler(ActionService actionService,  BotSettingRepository botSettingRepository) {
        this.actionService = actionService;
        botId = botSettingRepository.getOne("ZAYETS_USER").getTelegramId();
        botToken = botSettingRepository.getOne("ZAYETS_USER").getToken();
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
        return botId;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    //TODO: verify and implement
    private void handleIncomingMessage(Message message) throws TelegramApiException {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(message.getChatId());
        Action nextAction = actionService.getNextUserAction(message.getChatId(), message.getText());
        if (nextAction == null) {
            //TODO:
//            Action action = actionService.getStartUpAction();
//            actionService.updateLastUserAction(message.getChatId(), action.getId());
            sendMessageRequest.setReplyMarkup(startTheGameKeyboard());
            sendMessageRequest.setText("Hello world!");
            return;
        }

        actionService.updateLastUserAction(message.getChatId(), nextAction.getId());
        sendMessageRequest.setReplyMarkup(getPossibleResponseKeyboard(nextAction));
        String text = nextAction.getText();
        sendMessageRequest.setText(text);
        execute(sendMessageRequest);
    }

    //TODO: implement
    private static ReplyKeyboardMarkup startTheGameKeyboard() {
        return null;
    }

    private static ReplyKeyboardMarkup getPossibleResponseKeyboard(Action action) {
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
