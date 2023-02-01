package com.example.sideproject1.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingUser {
    private int currentPage;

    private long totalItems;

    private int totalPages;

    private List<UserResponse> users;
}
