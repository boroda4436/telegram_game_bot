package com.cbs.telegram.bot.telegram_api_connector.repository;

import com.cbs.telegram.bot.telegram_api_connector.entity.UserLastAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLastActionRepository extends JpaRepository<UserLastAction, Long> {}
