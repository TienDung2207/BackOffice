package com.example.sideproject1.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String username;

    private String fullName;

    private String email;

    private String roleName;

    private Integer status;

    private List<String> allRoleName;

    public UserResponse(String username, String fullName, String email, String roleName, Integer status) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roleName = roleName;
        this.status = status;
    }
}
