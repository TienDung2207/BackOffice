package com.example.sideproject1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Integer payId;

    private Double amount;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "pay_date")
    private Date payDate;

    private String status;

    @Column(name = "ip_address")
    private String ipAddress;

}
