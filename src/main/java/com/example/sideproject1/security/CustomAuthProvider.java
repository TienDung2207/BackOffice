package com.example.sideproject1.security;

import com.example.sideproject1.config.Config;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.entities.User;
import com.example.sideproject1.repositories.RoleRepository;
import com.example.sideproject1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountLockedException;
import java.util.Arrays;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Config config;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("UserName: " + name);
        User user = userRepository.getByUserName(name);

        if (user != null) {
            String hashKey = user.getHashKey();
            String userPass = user.getHashedPass();
            String hashedPass = config.hmacSHA512(hashKey, password);

            if (userPass.equals(hashedPass)) {
                if(user.getStatus() != null && user.getStatus() == 0) {
                    Role roleFromUser = roleRepository.getByRoleId(user.getRoleId());
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleFromUser.getRoleDesc());
                    log.info("" + authority);
                    return new UsernamePasswordAuthenticationToken(name, hashedPass, Arrays.asList(authority));
                }
                else {
                    throw new LockedException("An account was locked!");
                }
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } else {
            throw new UsernameNotFoundException("Could not find user");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
