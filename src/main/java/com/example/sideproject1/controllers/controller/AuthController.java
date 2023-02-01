package com.example.sideproject1.controllers.controller;

import com.example.sideproject1.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String logIn() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        List<String> roleNames = authService.registerPage();

        model.addAttribute("roleNames", roleNames);
        return "register";
    }
}
