package com.example.auth.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.entity.AuthResponse;
import com.example.auth.entity.Code;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.entity.UserRegisterDTO;
import com.example.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;

    private int exp = 240000;

    private int refreshExp = 240000;

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }


    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }


    public void validateToken(String token)
    {
        jwtService.validateToken(token);
    }
/* 
    public void validateToken(HttpServletRequest request) throws ExpiredJwtException, IllegalArgumentException{
        String token = null;
        String refresh = null;
        for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
            if (value.getName().equals("token")) {
                token = value.getValue();
            } else if (value.getName().equals("refresh")) {
                refresh = value.getValue();
            }
        }
        try {
            jwtService.validateToken(token);
        }catch (IllegalArgumentException | ExpiredJwtException e){
            jwtService.validateToken(refresh);
        }

    }
    */


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
            org.springframework.security.core.Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) 
            {
                
                Cookie refresh = cookieService.genereteCookie("refresh", generateToken(authRequest.getUsername()), refreshExp);
                Cookie cookie = cookieService.genereteCookie("token", generateToken(authRequest.getUsername()), exp);
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
                return ResponseEntity.ok(new AuthResponse(Code.A1));
            }
        }



        return ResponseEntity.ok(new AuthResponse(Code.A2));
    }

}


