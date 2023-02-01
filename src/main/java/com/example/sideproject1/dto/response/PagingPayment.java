package com.example.sideproject1.dto.response;

import com.example.sideproject1.entities.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingPayment {
    private int currentPage;

    private long totalItems;

    private int totalPages;

    private List<Payment> payments;
}
