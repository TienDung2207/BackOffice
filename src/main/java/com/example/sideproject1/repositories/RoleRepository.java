package com.example.sideproject1.repositories;

import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getByRoleName(String roleName);

    Role getByRoleDesc(String roleDesc);

    Role getByRoleId(int id);

    Boolean existsByRoleName(String roleName);

    @Query("select r from Role r INNER JOIN RoleAction rc on r.roleId = rc.roleId where rc.actionId = :actionId")
    List<Role> findByActionId(int actionId);

    @Query("select r from Role r where"
            + "((r.roleName like concat('%', :roleName, '%')) or (:roleName is null ) or (:roleName = ''))"
            + "and ((r.roleDesc like concat('%', :roleDesc,'%')) or (:roleDesc is null ) or (:roleDesc = ''))")
    Page<Role> findAllRole(String roleName, String roleDesc, Pageable pageable);

}
