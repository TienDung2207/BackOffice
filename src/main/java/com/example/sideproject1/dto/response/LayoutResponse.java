package com.example.sideproject1.dto.response;

import com.example.sideproject1.dto.RoleData;
import com.example.sideproject1.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class LayoutResponse {
    private List<Role> allRoles;

    private String roleName;

    private String roleDesc;

    private List<RoleData> actionsByRole;
}
