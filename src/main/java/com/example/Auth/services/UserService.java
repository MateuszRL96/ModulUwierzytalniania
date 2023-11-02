package com.example.Auth.services;

import org.springframework.stereotype.Service;

import com.example.Auth.entity.User;
import com.example.Auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }

}
