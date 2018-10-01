package com.cbs.telegram.bot.telegram_api_connector.repository;

import com.cbs.telegram.bot.telegram_api_connector.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message getByTextAndBotSettingName(String text, String botName);
}
