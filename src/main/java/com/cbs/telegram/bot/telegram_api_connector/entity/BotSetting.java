package com.cbs.telegram.bot.telegram_api_connector.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bot_setting")
public class BotSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bot_setting_id")
    private Long id;
    private String name;
    private String token;
}
