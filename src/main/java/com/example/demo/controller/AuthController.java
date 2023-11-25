package com.proyecto.onlybooks.controller;

import com.proyecto.onlybooks.entity.AuthResponse;
import com.proyecto.onlybooks.entity.LoginRequest;
import com.proyecto.onlybooks.entity.RegisterRequest;
import com.proyecto.onlybooks.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request) );
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws Exception
    {
        return ResponseEntity.ok(authService.register(request) );
    }

}
