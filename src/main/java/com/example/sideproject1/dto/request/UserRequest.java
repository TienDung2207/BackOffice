package com.example.sideproject1.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;

    private String password;

    private String email;

    private String fullName;

    private String role;
}
