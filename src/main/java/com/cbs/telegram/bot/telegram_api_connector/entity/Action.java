package com.cbs.telegram.bot.telegram_api_connector.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Action parent;
    private List<Action> children = new ArrayList<>();
    private BotSetting botSetting;

    @Id
    @Column(name = "action_id")
    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "parent_action_id")
    public Action getParent() {
        return parent;
    }

    @OneToMany(mappedBy = "parent", targetEntity = Action.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    public List<Action> getChildren() {
        return children;
    }

    @ManyToOne
    @JoinColumn(name = "bot_setting_id")
    public BotSetting getBotSetting() {
        return botSetting;
    }
}
