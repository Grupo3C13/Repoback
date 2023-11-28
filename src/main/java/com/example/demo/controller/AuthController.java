
package com.example.demo.controller;

import com.example.demo.entity.SignUpRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.LoginRequest;
import com.example.demo.entity.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:8090")
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
    public ResponseEntity<AuthResponse> register(@RequestBody SignUpRequest request) throws Exception
    {
        return ResponseEntity.ok(authService.register(request) );
    }

}
