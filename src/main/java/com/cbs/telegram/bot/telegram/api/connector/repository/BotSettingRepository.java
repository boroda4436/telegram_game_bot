package com.cbs.telegram.bot.telegram.api.connector.repository;

import com.cbs.telegram.bot.telegram.api.connector.entity.BotSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotSettingRepository extends JpaRepository<BotSetting, String> {

}
