package com.example.sideproject1.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSearch {
    private Double amount;

    private String orderInfo;

    private String status;

    private String ipAddress;
}
