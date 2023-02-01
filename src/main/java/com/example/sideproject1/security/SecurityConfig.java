package com.example.sideproject1.security;

import com.example.sideproject1.entities.Function;
import com.example.sideproject1.entities.Role;
import com.example.sideproject1.repositories.ActionRepository;
import com.example.sideproject1.repositories.FunctionRepository;
import com.example.sideproject1.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final String[] staticResources = {
            "/css/**",
            "/img/**",
            "/js/**",};
    private final CustomAuthProvider customAuthProvider;
    private final FunctionRepository functionRepository;
    private final RoleRepository roleRepository;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAccessDesignHandler customAccessDesignHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
                .authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/home/login")
                .defaultSuccessUrl("/home")
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDesignHandler)
                .and()
                .httpBasic();

        List<Function> functions = functionRepository.findAll();
        for(Function function : functions) {
            if(function.getActionId() != null) {
                List<Role> roles = roleRepository.findByActionId(function.getActionId());
                StringBuilder hasRole = new StringBuilder();
                if(roles != null) {
                    for(Role role : roles) {
                        hasRole.append("'" + role.getRoleDesc() + "'");
                        if(roles.indexOf(role) != (roles.size() - 1)) {
                            hasRole.append(", ");
                        }
                    }
                }
                http.authorizeRequests().antMatchers(function.getLink()).access("hasAnyRole(" + hasRole +")");
                log.info(function.getLink());
                log.info(hasRole.toString());
                log.info("==============================================");
            }
            else {
                http.authorizeRequests().antMatchers(function.getLink()).permitAll();
            }
        }

        http.authorizeRequests().anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthProvider);

        return authenticationManagerBuilder.build();
    }
}
