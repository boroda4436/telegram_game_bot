package com.cbs.telegram.bot.telegram_api_connector.controller;

import com.cbs.telegram.bot.telegram_api_connector.entity.Action;
import com.cbs.telegram.bot.telegram_api_connector.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(value = "/action")
public class ActionController {
    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping("/next")
    public Action getNextAction(String message) {
        return actionService.getNextAction(message);
    }

    @PostMapping("/add")
    public Action addChild(Long actionId, String text) {
        return actionService.addChild(actionId, text);
    }

    @DeleteMapping("/delete")
    public void delete(Long actionId) {
        actionService.delete(actionId);
    }
}
