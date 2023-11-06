package com.example.Auth.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Auth.entity.AuthResponse;
import com.example.Auth.entity.Code;
import com.example.Auth.entity.Role;
import com.example.Auth.entity.User;
import com.example.Auth.entity.UserRegisterDTO;
import com.example.Auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    private final JwtService jwtService;

    private final CookieService cookieService;

    private int exp = 240000;

    private int refreshExp = 240000;

    private final AuthenticationManager authenticationManager;


    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token)
    {
        jwtService.validateToken(token);
    }

    public void register(UserRegisterDTO userRegisterDTO) {

        User user = new User();
        user.setLogin(userRegisterDTO.getLogin());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());

        if (userRegisterDTO.getRole() != null)
        {
            user.setRole(userRegisterDTO.getRole());
        }
        else
        {
            user.setRole(Role.USER);
        }

        saveUser(user);
        
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {

        User user = userRepository.findUserByLogin(authRequest.getUsername()).orElse(null);

        if(user != null)
        {
            Authentication authenticate = (Authentication) authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if(((org.springframework.security.core.Authentication) authenticate).isAuthenticated())
            {
                Cookie cookie = cookieService.genereteCookie("token", generateToken(authRequest.getUsername()), exp);
                Cookie refresh = cookieService.genereteCookie("refresh", generateToken(authRequest.getUsername()), refreshExp);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.ok(
                    UserRegisterDTO
                    .builder()
                    .login(user.getUsername())
                    .email(user.getEmail())
                    .build());
            }
            else
            {
                return ResponseEntity.ok(new AuthResponse(null, Code.A1));
            }
        }



        return ResponseEntity.ok(new AuthResponse(null, Code.A2));
    }

}
