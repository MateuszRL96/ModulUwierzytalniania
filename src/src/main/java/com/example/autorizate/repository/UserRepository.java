package com.example.autorizate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autorizate.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findUserByLogin(String username);
    
}
