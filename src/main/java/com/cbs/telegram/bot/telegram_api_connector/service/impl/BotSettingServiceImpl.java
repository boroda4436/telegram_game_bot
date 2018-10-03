package com.cbs.telegram.bot.telegram_api_connector.service.impl;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.entity.BotSetting;
import com.cbs.telegram.bot.telegram_api_connector.repository.ActionRepository;
import com.cbs.telegram.bot.telegram_api_connector.repository.BotSettingRepository;
import com.cbs.telegram.bot.telegram_api_connector.service.BotSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotSettingServiceImpl implements BotSettingService {
    private final BotSettingRepository botSettingRepository;
    private final ActionRepository actionRepository;

    @Autowired
    public BotSettingServiceImpl(BotSettingRepository botSettingRepository, ActionRepository actionRepository) {
        this.botSettingRepository = botSettingRepository;
        this.actionRepository = actionRepository;
    }

    @Override
    public void createBot(String botName, String botToken, String welcomeMsg) {
        BotSetting botSetting = new BotSetting();
        botSetting.setName(botName);
        botSetting.setToken(botToken);
        botSettingRepository.save(botSetting);

        Action action = new Action();
        action.setText(welcomeMsg);
        action.setBotName(botName);
        actionRepository.save(action);
    }
}
