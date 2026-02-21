package com.app.ecommerce.controller;

import com.app.ecommerce.security.request.LoginRequest;
import com.app.ecommerce.security.request.SignupRequest;
import com.app.ecommerce.security.response.AuthResponse;
import com.app.ecommerce.security.response.MessageResponse;
import com.app.ecommerce.security.response.UserInfoResponse;
import com.app.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        AuthResponse response = authService.signinUser(loginRequest);
        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, response.getCookie())
                .body(response.getUserInfoResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signupUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authService.signout())
                .body(new MessageResponse("You have been signed out!"));
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getCurrentUserDetails(Authentication authentication) {
        UserInfoResponse response = authService.getCurrentUserDetails(authentication);
        return ResponseEntity.ok().body(response);
    }

}
