package com.example.sideproject1.services;

import com.example.sideproject1.entities.Action;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.User;
import com.example.sideproject1.repositories.ActionRepository;
import com.example.sideproject1.repositories.RoleRepository;
import com.example.sideproject1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class ActionService {
    private final ActionRepository actionRepository;

    public Action getByName(String name) {
        Action action = actionRepository.getByActionName(name);
        return action;
    }
    public Action getByParentAction(String actionName, Action parentAction) {
        Action action = actionRepository.getByParentAction(actionName, parentAction.getActionId());
        return action;
    }

}
