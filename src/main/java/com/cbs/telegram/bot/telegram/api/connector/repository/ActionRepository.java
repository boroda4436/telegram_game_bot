package com.cbs.telegram.bot.telegram.api.connector.repository;

import com.cbs.telegram.bot.telegram.api.connector.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> { }
