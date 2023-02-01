package com.example.sideproject1.services;

import com.example.sideproject1.dto.RoleData;
import com.example.sideproject1.dto.request.RoleRequest;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.entities.Action;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.RoleAction;
import com.example.sideproject1.entities.User;
import com.example.sideproject1.repositories.ActionRepository;
import com.example.sideproject1.repositories.RoleActionRepository;
import com.example.sideproject1.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class RoleService {

    private final RoleActionRepository roleActionRepository;

    private final RoleRepository roleRepository;

    private final ActionRepository actionRepository;

    private final ActionService actionService;

    public List<Action> getChildren(List<Action> actions, Action action) {
        List<Action> newActions = new ArrayList<>();
        for(Action actionEle : actions) {
            if(actionEle.getParentId() == action.getActionId()) {
                newActions.add(actionEle);
            }
        }
        return newActions;
    }
    public List<RoleData> handleRoleData(String roleName) {
        Role role = roleRepository.getByRoleName(roleName);
        List<Action> actionsByRole = actionRepository.findByRoleId(role.getRoleId());

        List<RoleData> dataResponse = new ArrayList<>();
        for (Action action : actionsByRole) {
            if (action.getParentId() == null) {
                RoleData data1 = new RoleData(action.getActionName());
                dataResponse.add(data1);
                if(getChildren(actionsByRole, action) != null) {
                    for(Action action1 : getChildren(actionsByRole, action)) {
                        if(actionsByRole.contains(action1)) {
                            RoleData data2 = new RoleData(action1.getActionName());
                            if(data1.getData() == null) {
                                List<RoleData> newRoleData = new ArrayList<>();
                                newRoleData.add(data2);
                                data1.setData(newRoleData);
                            }
                            else  {
                                data1.getData().add(data2);
                            }

                            if(getChildren(actionsByRole, action1) != null) {
                                for(Action action2 : getChildren(actionsByRole, action1)) {
                                    if(actionsByRole.contains(action2)) {
                                        RoleData data3 = new RoleData(action2.getActionName(), null);
                                        if(data2.getData() == null) {
                                            List<RoleData> newRoleData = new ArrayList<>();
                                            newRoleData.add(data3);
                                            data2.setData(newRoleData);
                                        }
                                        else  {
                                            data2.getData().add(data3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return dataResponse;
    }

    public Map<String, List> updatePermissionPage() {
        List<Action> allActions = actionRepository.findAll();
        List<RoleData> dataResponse = new ArrayList<>();
        for (Action action : allActions) {
            if (action.getParentId() == null) {
                RoleData data1 = new RoleData(action.getActionName());
                dataResponse.add(data1);
                if(getChildren(allActions, action) != null) {
                    for(Action action1 : getChildren(allActions, action)) {
                        if(allActions.contains(action1)) {
                            RoleData data2 = new RoleData(action1.getActionName());
                            if(data1.getData() == null) {
                                List<RoleData> newRoleData = new ArrayList<>();
                                newRoleData.add(data2);
                                data1.setData(newRoleData);
                            }
                            else  {
                                data1.getData().add(data2);
                            }

                            if(getChildren(allActions, action1) != null) {
                                for(Action action2 : getChildren(allActions, action1)) {
                                    if(allActions.contains(action2)) {
                                        RoleData data3 = new RoleData(action2.getActionName(), null);
                                        if(data2.getData() == null) {
                                            List<RoleData> newRoleData = new ArrayList<>();
                                            newRoleData.add(data3);
                                            data2.setData(newRoleData);
                                        }
                                        else  {
                                            data2.getData().add(data3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Map<String, List> response = new HashMap<>();
        response.put("actionsNoParent", dataResponse);

        return response;
    }

    public ResponseEntity<ResponseMsg> handleUpdatePermissions(RoleRequest roleRequest) {
        try {
            String roleName = roleRequest.getRoleName();
            Role role = roleRepository.getByRoleName(roleName);
            if(role == null) {
                return new ResponseEntity<>(new ResponseMsg("01", "Could not find role"), HttpStatus.BAD_REQUEST);
            }

            List<Integer> newActions = new ArrayList<>();
            for(RoleData a : roleRequest.getData()) {
                Action parentAction = actionService.getByName(a.getActionName());
                newActions.add(parentAction.getActionId());

                if(a.getData().size() > 0) {
                    for(RoleData b : a.getData()) {
                        Action childAction = actionService.getByParentAction(b.getActionName(), parentAction);
                        newActions.add(childAction.getActionId());

                        if(b.getData().size() > 0) {
                            for(RoleData c : b.getData()) {
                                Action childOfChildAction = actionService.getByParentAction(c.getActionName(), childAction);
                                newActions.add(childOfChildAction.getActionId());
                            }
                        }
                    }
                }
            }

            List<RoleAction> roleActionDB = roleActionRepository.findByRoleId(role.getRoleId());
            List<Integer> actionIdDB = new ArrayList<>();
            for(RoleAction item : roleActionDB) {
                actionIdDB.add(item.getActionId());
            }

            List<Integer> newActionsAfterLoop = newActions;
            for(RoleAction roleAction : roleActionDB) {
                if(newActions.contains(roleAction.getActionId())) {
                    roleAction.setRoleId(role.getRoleId());
                    roleActionRepository.save(roleAction);
                    newActionsAfterLoop.remove(roleAction.getActionId());
                }
                else {
                    roleActionRepository.delete(roleAction);
                }
            }
            if(newActionsAfterLoop.size() != 0) {
                for(Integer item : newActionsAfterLoop) {
                    RoleAction newRoleAction = new RoleAction();
                    newRoleAction.setActionId(item);
                    newRoleAction.setRoleId(role.getRoleId());
                    roleActionRepository.save(newRoleAction);
                }
            }

            return new ResponseEntity<>(new ResponseMsg("00", "update successfully!"), HttpStatus.OK);
        }
        catch (Exception ex) {
            log.error("update role ex: ", ex);
            return new ResponseEntity<>(new ResponseMsg("500", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<ResponseMsg> handleCreatePermissions(RoleRequest roleRequest) {
        try {
            String roleName =  roleRequest.getRoleName();
            if (!roleRepository.existsByRoleName(roleName)) {
                Set<Integer> newActions = new HashSet<>();
                for(RoleData a : roleRequest.getData()) {
                    Action parentAction = actionService.getByName(a.getActionName());
                    newActions.add(parentAction.getActionId());

                    if(a.getData().size() > 0) {
                        for(RoleData b : a.getData()) {
                            Action childAction = actionService.getByParentAction(b.getActionName(), parentAction);
                            newActions.add(childAction.getActionId());

                            if(b.getData().size() > 0) {
                                for(RoleData c : b.getData()) {
                                    Action childOfChildAction = actionService.getByParentAction(c.getActionName(), childAction);
                                    newActions.add(childOfChildAction.getActionId());
                                }
                            }
                        }
                    }
                }
                String roleDesc = "ROLE_" + roleName.toUpperCase();
                Role newRole = new Role();
                newRole.setRoleName(roleName);
                newRole.setRoleDesc(roleDesc);
                roleRepository.save(newRole);

                for(Integer item : newActions) {
                    RoleAction newRoleAction = new RoleAction();
                    newRoleAction.setActionId(item);
                    newRoleAction.setRoleId(newRole.getRoleId());
                    roleActionRepository.save(newRoleAction);
                }

                return new ResponseEntity<>(new ResponseMsg("00" , "Create Permission Successfully!"), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ResponseMsg("400" , "Permission already exist!"), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception ex) {
            log.info("create permissions ex: " , ex);
            return new ResponseEntity<>(new ResponseMsg("500" , ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseMsg> findPermissionsByName(String roleName) {
        if(!roleRepository.existsByRoleName(roleName)) {
            return new ResponseEntity<>(new ResponseMsg("400", "Could not find role"), HttpStatus.OK);
        }
        else {
            List<RoleData> dataResponse = this.handleRoleData(roleName);
            return new ResponseEntity<>(new ResponseMsg("00", "role exists", dataResponse), HttpStatus.OK);
        }
    }

    public Page<Role> pagingRole(int pageNumber, String roleName, String roleDesc) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 8, Sort.by("roleId").ascending());
        return roleRepository.findAllRole(roleName, roleDesc, pageable);
    }
}
