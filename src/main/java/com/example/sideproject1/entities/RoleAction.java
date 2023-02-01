package com.example.sideproject1.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "role_action")
@IdClass(RoleAction.class)
public class RoleAction implements Serializable {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Id
    @Column(name = "action_id")
    private Integer actionId;
}
