package com.example.sideproject1.controllers.controller;

import com.example.sideproject1.dto.response.LayoutResponse;
import com.example.sideproject1.services.LayoutService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class PageController {
    private final LayoutService layoutService;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        LayoutResponse layoutResponse = layoutService.handleLayout(authentication);

        model.addAttribute("actionsByRole", layoutResponse.getActionsByRole());
        return "index";
    }

}
