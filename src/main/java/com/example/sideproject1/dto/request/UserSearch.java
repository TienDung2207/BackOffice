package com.example.sideproject1.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearch {
    private String username;

    private String email;

    private String roleName;

    private Integer status;

    private int tableLength;
}
