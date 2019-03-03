package com.cbs.telegram.bot.telegram.api.connector.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

//TODO: move it to env variables

@Data
@Entity
@Table(name = "bot_setting")
public class BotSetting {
    @Id
    @Column(name = "bot_name")
    private String name;
    @Column(name = "telegram_bot_id")
    private String telegramId;
    private String token;
}
