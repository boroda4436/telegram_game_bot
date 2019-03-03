package com.cbs.telegram.bot.telegram.api.connector.dto;

import com.cbs.telegram.bot.telegram.api.connector.entity.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActionDto {
    private Long id;
    private Long parentId;
    private String text;
    private List<ActionDto> children = new ArrayList<>();


    public static ActionDto parseFromActionEntity(Action action) {
        if (action == null) {
            return null;
        }

        ActionDto dto = new ActionDto();
        dto.setId(action.getId());
        dto.setParentId(action.getParentId());
        dto.setText(action.getText());
        List<ActionDto> children = new ArrayList<>();
        action.getChildren().stream().filter(Objects::nonNull).forEach(child -> {
            ActionDto actionDto = new ActionDto();
            actionDto.setId(child.getId());
            actionDto.setParentId(child.getParentId());
            actionDto.setText(child.getText());
            children.add(actionDto);
        });
        dto.setChildren(children);
        return dto;
    }
}
