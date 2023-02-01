package com.example.sideproject1.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String amount;

    private String orderInfo;

}
