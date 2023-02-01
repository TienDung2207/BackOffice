package com.example.sideproject1.controllers.restcontroller;

import com.example.sideproject1.dto.request.RoleRequest;
import com.example.sideproject1.dto.request.UserRequest;
import com.example.sideproject1.dto.request.UserSearch;
import com.example.sideproject1.dto.response.PagingUser;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.dto.response.UserResponse;
import com.example.sideproject1.services.RoleService;
import com.example.sideproject1.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class RestSystemController {
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/system/accounts/update")
    public ResponseMsg handleUpdateAccount(@RequestBody UserRequest request) {
        ResponseMsg responseMsg = userService.updateUser(request);

        return responseMsg;
    }

    @PostMapping("/system/accounts/detail/{numberPage}")
    public PagingUser changePageNumber(@RequestBody UserSearch userSearch, @PathVariable("numberPage") int numberPage) {
        PagingUser pagingUser = userService.changeUserPage(userSearch, numberPage);

        return pagingUser;
    }

    @GetMapping("/system/accounts/reset-pass")
    public ResponseEntity<ResponseMsg> resetPassword(@RequestParam("username") String username) {
        ResponseEntity<ResponseMsg> response = userService.handleResetPass(username);

        return response;
    }

    @GetMapping("/system/accounts/lock-account")
    public ResponseEntity<ResponseMsg> lockAccount(@RequestParam("username") String username) {
        ResponseEntity<ResponseMsg> response = userService.handleLockAccount(username);

        return response;
    }

    @GetMapping("/system/accounts/unlock-account")
    public ResponseEntity<ResponseMsg> unlockAccount(@RequestParam("username") String username) {
        ResponseEntity<ResponseMsg> response = userService.handleUnlockAccount(username);

        return response;
    }

    @GetMapping("/system/accounts/info-update")
    public ResponseEntity<UserResponse> getInfoUpDate(@RequestParam("username") String username) {
        ResponseEntity<UserResponse> response = userService.getInfoUpDate(username);

        return response;
    }

    @PostMapping("/system/role/update")
    public ResponseEntity<ResponseMsg> updatePermissions(@RequestBody RoleRequest roleRequest) {
        ResponseEntity<ResponseMsg> responseMsg = roleService.handleUpdatePermissions(roleRequest);

        return responseMsg;
    }

    @PostMapping("/system/role/create")
    public ResponseEntity<ResponseMsg> createPermissions(@RequestBody RoleRequest roleRequest) {
        ResponseEntity<ResponseMsg> responseMsg = roleService.handleCreatePermissions(roleRequest);

        return responseMsg;
    }

    @GetMapping("/system/role")
    public ResponseEntity<ResponseMsg> findPerByName(@RequestParam("roleName") String roleName) {
        ResponseEntity<ResponseMsg> responseMsg = roleService.findPermissionsByName(roleName);

        return responseMsg;
    }
}
