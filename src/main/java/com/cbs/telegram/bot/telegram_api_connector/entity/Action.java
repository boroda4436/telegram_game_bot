package com.cbs.telegram.bot.telegram_api_connector.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @Column(name = "action_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;
    private Action parent;
    private List<Action> children = new ArrayList<>();

    @Id
    @Column(name = "action_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_action_id")
    @JsonBackReference
    public Action getParent() {
        return parent;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", targetEntity = Action.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Action> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", parentId=" + getParentId() +
                ", children=" + children +
                '}';
    }

    @Transient
    public Long getParentId() {
        return parent == null ? null : parent.getId();
    }
}
