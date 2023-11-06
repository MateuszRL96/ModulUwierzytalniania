package com.example.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

    Optional<User> findUserByLogin(String login);
    
}
