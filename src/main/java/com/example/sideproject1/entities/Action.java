package com.example.sideproject1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "action")
public class Action {
    @Id
    @Column(name = "action_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actionId;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "parent_id")
    private Integer parentId;
}
