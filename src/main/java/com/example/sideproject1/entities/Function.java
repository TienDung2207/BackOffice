package com.example.sideproject1.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "function")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funcId")
    private Integer functionId;

    private String method;

    private String link;

    @Column(name = "action_id")
    private Integer actionId;

}
