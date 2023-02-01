package com.example.sideproject1.repositories;

import com.example.sideproject1.entities.RoleAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleActionRepository extends JpaRepository<RoleAction, Integer> {
    List<RoleAction> findByRoleId(int roleId);
}
