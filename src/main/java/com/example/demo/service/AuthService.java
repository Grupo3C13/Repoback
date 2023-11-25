package com.example.demo.service.implement;

import com.example.demo.entity.*;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtServiceImp jwtServiceImp;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user=userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token=jwtServiceImp.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(SignUpRequest request) throws Exception {
        Optional<User> found = userRepository.findByEmail(request.getEmail());
        if(found.isEmpty()){
            User user = User.builder()
                    .surname(request.getLastname())
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode( request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            return AuthResponse.builder()
                    .token(jwtServiceImp.getToken(user))
                    .build();
        }else {
            throw new Exception("Email esta en uso");
        }
    }
}