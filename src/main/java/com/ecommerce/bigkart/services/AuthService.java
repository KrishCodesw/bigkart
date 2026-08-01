package com.ecommerce.bigkart.services;

import com.ecommerce.bigkart.dto.AuthResponseDTO;
import com.ecommerce.bigkart.dto.LoginDTO;
import com.ecommerce.bigkart.dto.RegisterDTO;
import com.ecommerce.bigkart.entities.Role;
import com.ecommerce.bigkart.entities.UserEntity;
import com.ecommerce.bigkart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder

    public String register(RegisterDTO dto) {
    if(userRepository.findByUsername(dto.getUsername()).isPresent()){
        return "Username already exists";
    }
        UserEntity user= new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role=(dto.getRole()!=null && dto.getRole().equalsIgnoreCase("ADMIN")?)
                user.setRole(role);

    }

    public AuthResponseDTO login(LoginDTO dto) {
    }
}
