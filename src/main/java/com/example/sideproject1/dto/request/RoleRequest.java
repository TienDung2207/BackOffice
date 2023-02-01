package com.example.sideproject1.dto.request;

import com.example.sideproject1.dto.RoleData;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleRequest {
    private Set<RoleData> data;

    private String roleName;
}
