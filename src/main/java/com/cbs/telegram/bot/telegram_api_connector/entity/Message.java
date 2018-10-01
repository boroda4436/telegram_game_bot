package com.cbs.telegram.bot.telegram_api_connector.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Message parent;
    private List<Message> children = new ArrayList<Message>();
    private BotSetting botSetting;

    @Id
    @Column(name = "message_id")
    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "parent_message_id")
    public Message getParent() {
        return parent;
    }

    @OneToMany(mappedBy = "parent", targetEntity = Message.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    public List<Message> getChildren() {
        return children;
    }

    @ManyToOne
    @JoinColumn(name = "bot_setting_id")
    public BotSetting getBotSetting() {
        return botSetting;
    }
}
