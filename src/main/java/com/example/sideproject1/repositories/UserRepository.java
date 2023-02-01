package com.example.sideproject1.repositories;

import com.example.sideproject1.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getByUserName(String name);
    boolean existsByUserName(String name);
    @Query("select u from User u where"
            + "((u.userName like concat('%', :username, '%')) or (:username is null ) or (:username = ''))"
            + "and ((u.email like concat('%', :email,'%')) or (:email is null ) or (:email = ''))"
            + "and ((u.roleId = :roleId) or (:roleId is null))"
            + "and ((u.status = :status) or (:status is null))")
    Page<User> findAllUser(String username, String email, Integer roleId, Integer status, Pageable pageable);
}
