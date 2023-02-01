package com.example.sideproject1.services;

//import com.example.sideproject1.Filter.TokenProvider;
//import com.example.sideproject1.Filter.TokenProvider;

import com.example.sideproject1.config.Config;
import com.example.sideproject1.dto.request.UserRequest;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.User;
import com.example.sideproject1.repositories.RoleRepository;
import com.example.sideproject1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final Config config;
    private final RoleRepository roleRepository;;

    public List<String> registerPage() {
        List<String> roleNames = new ArrayList<>();
        List<Role> allRoles = roleRepository.findAll();
        for(Role role : allRoles) {
            roleNames.add(role.getRoleName());
        }

        return roleNames;
    }

    public ResponseEntity<ResponseMsg> handleRegister(UserRequest request) {
        ResponseMsg responseMsg = new ResponseMsg();
        String name = request.getUsername();
        if (userRepository.existsByUserName(name)) {
            responseMsg.setRspCode("400");
            responseMsg.setRspMessage("Username is already taken");

            return new ResponseEntity<>(responseMsg, HttpStatus.BAD_REQUEST);
        }

        try {
            String password = request.getPassword();
            String hashKey = config.getRandomKey(32);
            String hashPass = config.hmacSHA512(hashKey, password);

            User user = new User();
            user.setUserName(name);
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setHashKey(hashKey);
            user.setHashedPass(hashPass);
            user.setStatus(0);

            String roleName = request.getRole();
            if(!roleRepository.existsByRoleName(roleName)) {
                return new ResponseEntity<>(new ResponseMsg("400", "rolename does not match"), HttpStatus.BAD_REQUEST);
            }

            Role userRole = roleRepository.getByRoleName(roleName);
            user.setRoleId(userRole.getRoleId());
            userRepository.save(user);
            log.info("User registered successfully!");

            return new ResponseEntity<>(new ResponseMsg("00", "User registered successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            log.error("handleRegister ex: ", e);

            return new ResponseEntity<>(new ResponseMsg("500", "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
