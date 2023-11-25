package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    RoleService roleService;

//    @Autowired
//    EmailValidator emailValidator;

//    @Autowired
//    ConfirmationTokenService tokenService;

//    @Autowired
//    UserRepository userRepository;

//    @Autowired
//    EmailService emailService;

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

}
