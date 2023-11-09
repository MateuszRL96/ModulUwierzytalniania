package com.example.autorizate.services;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.autorizate.entity.AuthResponse;
import com.example.autorizate.entity.Code;
import com.example.autorizate.entity.Role;
import com.example.autorizate.entity.User;
import com.example.autorizate.entity.UserRegisterDTO;
import com.example.autorizate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor//wstrzykniecie interfejsu repozytorium
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookiService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;


    private User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);//zapisuje w bazie danych po czym zwraca od razu wynik z bazy danych, otrzymamy id usera
    }

    public String generateToken(String username, int exp){
        return jwtService.generateToken(username, exp);
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
        if(userRegisterDTO.getRole() != null){
            user.setRole(userRegisterDTO.getRole());
        }
        else{
            user.setRole(Role.USER);
        }
        saveUser(user);
    }

 public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findUserByLogin(authRequest.getUsername()).orElse(null);
        if (user != null) {
            AuthenticationManager authenticate = (AuthenticationManager) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (((org.springframework.security.core.Authentication) authenticate).isAuthenticated()) {
                javax.servlet.http.Cookie refresh = cookiService.generateCookie("refresh", generateToken(authRequest.getUsername(),refreshExp), refreshExp);
                javax.servlet.http.Cookie cookie = cookiService.generateCookie("token", generateToken(authRequest.getUsername(),exp), exp);
                response.addCookie(cookie);
                response.addCookie(refresh);
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build());
            } else {
                return ResponseEntity.ok(new AuthResponse(null, Code.A1));
            }
        }
        return ResponseEntity.ok(new AuthResponse(null, Code.A2));
    }
}
