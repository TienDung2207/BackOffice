package com.example.sideproject1.services;

import com.example.sideproject1.config.Config;
import com.example.sideproject1.dto.request.UserRequest;
import com.example.sideproject1.dto.request.UserSearch;
import com.example.sideproject1.dto.response.PagingUser;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.dto.response.UserResponse;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.User;
import com.example.sideproject1.repositories.RoleRepository;
import com.example.sideproject1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Config config;

    public User getUserByName(String username) {
        return userRepository.getByUserName(username);
    }

    public ResponseMsg updateUser(UserRequest request) {
        try {
            User user = userRepository.getByUserName(request.getUsername());
            Role role = roleRepository.getByRoleName(request.getRole());

            user.setRoleId(role.getRoleId());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            if(!request.getPassword().isEmpty()) {
                user.setHashedPass(config.hmacSHA512(user.getHashKey(), request.getPassword()));
            }
            userRepository.save(user);
            ResponseMsg responseMsg = new ResponseMsg("00", "Account updated successfully!");

            return responseMsg;
        }
        catch (Exception ex) {
            log.error("updateUser ex: ", ex);
            return new ResponseMsg("500", "Internal Server Error");
        }
    }

    public Page<User> pagingUser(int pageNumber, String username, String email, String roleName, Integer status, int tableLength) {
        Pageable pageable = PageRequest.of(pageNumber - 1, tableLength, Sort.by("userId").ascending());
        Role role = roleRepository.getByRoleName(roleName);
        if(role != null) {
            return userRepository.findAllUser(username, email, role.getRoleId(), status, pageable);
        }
        else {
            return userRepository.findAllUser(username, email, null, status, pageable);
        }
    }
    public PagingUser changeUserPage(UserSearch userSearch, int numberPage) {
        Page<User> page;
        if (userSearch.getEmail().isEmpty() && userSearch.getUsername().isEmpty() && userSearch.getRoleName().isEmpty() && userSearch.getStatus() == null) {
            page = this.pagingUser(numberPage, "", "", "", null, userSearch.getTableLength());
        } else {
            page = this.pagingUser(numberPage, userSearch.getUsername(), userSearch.getEmail(), userSearch.getRoleName(), userSearch.getStatus(), userSearch.getTableLength());
        }

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<User> users = page.getContent();

        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setEmail(user.getEmail());
            userResponse.setUsername(user.getUserName());
            userResponse.setFullName(user.getFullName());
            userResponse.setRoleName(roleRepository.getByRoleId(user.getRoleId()).getRoleName());
            userResponse.setStatus(user.getStatus());

            userResponses.add(userResponse);
        }

        PagingUser pagingUser = new PagingUser();
        pagingUser.setCurrentPage(numberPage);
        pagingUser.setTotalPages(totalPages);
        pagingUser.setTotalItems(totalItems);
        pagingUser.setUsers(userResponses);

        return pagingUser;
    }

    public ResponseEntity<ResponseMsg> handleResetPass(String username) {
        try {
            if(userRepository.existsByUserName(username)) {
                User user = userRepository.getByUserName(username);
                String newPass = "111";
                user.setHashedPass(config.hmacSHA512(user.getHashKey(), config.md5(newPass)));
                userRepository.save(user);

                return new ResponseEntity<>(new ResponseMsg("00", "Reset password successfully!"), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ResponseMsg("400", "Could not find user"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            log.error("handle reset pass ex: " + exception);
            return new ResponseEntity<>(new ResponseMsg("500", "Reset pass fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<ResponseMsg> handleLockAccount(String username) {
        try {
            if(userRepository.existsByUserName(username)) {
                User user = userRepository.getByUserName(username);
                user.setStatus(1);
                userRepository.save(user);

                return new ResponseEntity<>(new ResponseMsg("00", "Lock Account successfully!"), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ResponseMsg("400", "Could not find user"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            log.error("handle reset pass ex: " + exception);
            return new ResponseEntity<>(new ResponseMsg("500", "Lock account fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<UserResponse> getInfoUpDate(String username) {
        try {
            if(userRepository.existsByUserName(username)) {
                User user = userRepository.getByUserName(username);
                Role role = roleRepository.getByRoleId(user.getRoleId());
                List<Role> allRoles = roleRepository.findAll();
                List<String> allRoleName = new ArrayList<>();
                for(Role r : allRoles) {
                    allRoleName.add(r.getRoleName());
                }
                UserResponse userResponse = new UserResponse();
                userResponse.setUsername(user.getUserName());
                userResponse.setEmail(user.getEmail());
                userResponse.setStatus(user.getStatus());
                userResponse.setFullName(user.getFullName());
                userResponse.setAllRoleName(allRoleName);
                userResponse.setRoleName(role.getRoleName());

                return new ResponseEntity<>(userResponse, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new UserResponse(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            log.error("handle reset pass ex: " + exception);
            return new ResponseEntity<>(new UserResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseMsg> handleUnlockAccount(String username) {
        try {
            if(userRepository.existsByUserName(username)) {
                User user = userRepository.getByUserName(username);
                user.setStatus(0);
                userRepository.save(user);

                return new ResponseEntity<>(new ResponseMsg("00", "Unlock Account successfully!"), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ResponseMsg("400", "Could not find user"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            log.error("handle reset pass ex: " + exception);
            return new ResponseEntity<>(new ResponseMsg("500", "Lock account fail"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<UserResponse> handleAccountDetailPage(Page<User> page) {
        List<UserResponse> users = new ArrayList<>();
        for(User item : page) {
            UserResponse user = new UserResponse();
            Role role = roleRepository.getByRoleId(item.getRoleId());
            user.setUsername(item.getUserName());
            user.setStatus(item.getStatus());
            user.setEmail(item.getEmail());
            user.setFullName(item.getFullName());
            user.setRoleName(role.getRoleName());

            users.add(user);
        }

        return users;
    }
}
