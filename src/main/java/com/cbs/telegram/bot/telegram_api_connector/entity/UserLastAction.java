package com.cbs.telegram.bot.telegram_api_connector.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "user_last_actions")
public class UserLastAction {
    @Id
    @Column(name = "user_chat_id", unique = true)
    private Long chartId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "last_action_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Action action;
}
