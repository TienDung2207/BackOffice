package com.example.sideproject1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleData {
    private String actionName;

    private List<RoleData> data;

    public RoleData(String actionName) {
        this.actionName = actionName;
    }
}
