package com.example.sideproject1.controllers.restcontroller;

import com.example.sideproject1.dto.request.UserRequest;
import com.example.sideproject1.dto.response.ResponseMsg;
import com.example.sideproject1.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("auth")
@AllArgsConstructor
public class RestAuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMsg> handleRegister(@RequestBody UserRequest request) {
        ResponseEntity<ResponseMsg> message = authService.handleRegister(request);

        return message;
    }
}
