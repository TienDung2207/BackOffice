package com.example.sideproject1.services;

import com.example.sideproject1.dto.RoleData;
import com.example.sideproject1.dto.response.LayoutResponse;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class LayoutService {
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public LayoutResponse handleLayout(Authentication authentication) {
        String roleDesc = "";
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            roleDesc = auth.getAuthority();
        }
        log.info("role:" + roleDesc);

        List<Role> allRoles = roleRepository.findAll();
        Role role = roleRepository.getByRoleDesc(roleDesc);
        List<RoleData> dataResponse = roleService.handleRoleData(role.getRoleName());
        log.error("XIN CHAO`");

        LayoutResponse layoutResponse = new LayoutResponse();
        layoutResponse.setAllRoles(allRoles);
        layoutResponse.setRoleName(role.getRoleName());
        layoutResponse.setRoleDesc(roleDesc);
        layoutResponse.setActionsByRole(dataResponse);

        return layoutResponse;
    }
}
