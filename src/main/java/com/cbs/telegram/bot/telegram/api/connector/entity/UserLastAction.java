package com.cbs.telegram.bot.telegram.api.connector.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
