package com.example.sideproject1.repositories;

import com.example.sideproject1.entities.Action;
import com.example.sideproject1.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ActionRepository extends JpaRepository<Action, Integer> {
    Action getByActionName(String name);

    @Query("select a from Action a where"
            + "(a.actionName = :actionName) AND (a.parentId = :actionId)")
    Action getByParentAction(String actionName, Integer actionId);

    @Query("select a from Action a INNER JOIN RoleAction rc on a.actionId = rc.actionId where rc.roleId = :roleId order by a.actionId asc")
    List<Action> findByRoleId(int roleId);

}
