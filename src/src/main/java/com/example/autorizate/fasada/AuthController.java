package com.example.autorizate.fasada;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.autorizate.entity.AuthResponse;
import com.example.autorizate.entity.Code;
import com.example.autorizate.entity.User;
import com.example.autorizate.entity.UserRegisterDTO;
import com.example.autorizate.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> addNewUser(@RequestBody UserRegisterDTO user)
    {
        userService.register(user);
        return ResponseEntity.ok(new AuthResponse(null, Code.SUCCESS));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response)
    {
        return userService.login(response, user);
    }
    
}
