package com.cbs.telegram.bot.telegram_api_connector.repository;

import com.cbs.telegram.bot.telegram_api_connector.entity.BotSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotSettingRepository extends JpaRepository<BotSetting, String> {

}
