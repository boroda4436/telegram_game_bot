package com.cbs.telegram.bot.telegram.api.connector.repository;

import com.cbs.telegram.bot.telegram.api.connector.entity.UserLastAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLastActionRepository extends JpaRepository<UserLastAction, Long> {}
