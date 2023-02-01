package com.example.sideproject1.controllers.controller;

import com.example.sideproject1.dto.RoleData;
import com.example.sideproject1.dto.response.LayoutResponse;
import com.example.sideproject1.dto.response.UserResponse;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.User;
import com.example.sideproject1.services.LayoutService;
import com.example.sideproject1.services.RoleService;
import com.example.sideproject1.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class SystemController {
    private final UserService userService;
    private final LayoutService layoutService;
    private final RoleService roleService;

    @GetMapping("/system/role/detail")
    public String permissionDetail(Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        Page<Role> page = roleService.pagingRole(1, "", "");

        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("roles", page.getContent());

        return "roles-detail";
    }

    @GetMapping("/system/role/create")
    public String createPermission(Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        Map<String, List> map = roleService.updatePermissionPage();

        model.addAttribute("actionsNoParent", map.get("actionsNoParent"));
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());

        return "create-role";
    }

    @GetMapping("/system/role/update")
    public String updatePermissions(@RequestParam("roleName") String roleName, Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        Map<String, List> map = roleService.updatePermissionPage();
        List<RoleData> dataResponse = roleService.handleRoleData(roleName);

        model.addAttribute("actionsNoParent", map.get("actionsNoParent"));
        model.addAttribute("allRole", layoutResponse.getAllRoles());
        model.addAttribute("roleName", roleName);
        model.addAttribute("roleDesc", "ROLE_" + roleName.toUpperCase());
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());
        model.addAttribute("actionsByRoleUpdate", dataResponse);

        return "update-role";
    }

    @GetMapping("/system/accounts/create")
    public String createAccount(Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);

        model.addAttribute("allRole", layoutResponse.getAllRoles());
        model.addAttribute("roleName", layoutResponse.getRoleName());
        model.addAttribute("roleDesc", layoutResponse.getRoleDesc());
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());
        return "create-account";
    }

    @GetMapping("/system/accounts/update")
    public String updateAccount(Authentication authentication, Model model, @RequestParam("username") String nameUpdate) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        User user = userService.getUserByName(nameUpdate);

        model.addAttribute("allRole", layoutResponse.getAllRoles());
        model.addAttribute("roleName", layoutResponse.getRoleName());
        model.addAttribute("roleDesc", layoutResponse.getRoleDesc());
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());
        model.addAttribute("user", user);

        return "update-account";
    }

    @GetMapping("/system/accounts/detail")
    public String accountDetail(Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);
        Page<User> page = userService.pagingUser(1, "", "", "", null, 5);
        List<UserResponse> users = userService.handleAccountDetailPage(page);

        model.addAttribute("allRole", layoutResponse.getAllRoles());
        model.addAttribute("roleName", layoutResponse.getRoleName());
        model.addAttribute("roleDesc", layoutResponse.getRoleDesc());
        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());

        model.addAttribute("currentPage", 1);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("users", users);

        return "account-detail";
    }
}
