package com.ecommerce.bigkart.controllers;

import com.ecommerce.bigkart.dto.AuthResponseDTO;
import com.ecommerce.bigkart.dto.LoginDTO;
import com.ecommerce.bigkart.dto.RegisterDTO;
import com.ecommerce.bigkart.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto){
        return authService.register(dto);
    }
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO dto){
        return authService.login(dto);
    }
}
