package com.app.ecommerce.controller;

import com.app.ecommerce.security.request.LoginRequest;
import com.app.ecommerce.security.request.SignupRequest;
import com.app.ecommerce.security.response.AuthResponse;
import com.app.ecommerce.security.response.MessageResponse;
import com.app.ecommerce.security.response.UserInfoResponse;
import com.app.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication related operations")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signin")
    @Operation(
            summary = "Authenticate User",
            description = "Validates user credentials and returns authenticated user details."
    )
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        AuthResponse response = authService.signinUser(loginRequest);
        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, response.getCookie())
                .body(response.getUserInfoResponse());
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Register User",
            description = "Creates a new user account with the provided signup data."
    )
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signupUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!!"));
    }

    @PostMapping("/signout")
    @Operation(
            summary = "Sign Out User",
            description = "Invalidates the current session cookie and signs the user out."
    )
    public ResponseEntity<?> signoutUser() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authService.signout())
                .body(new MessageResponse("You have been signed out!"));
    }

    @GetMapping("/user")
    @Operation(
            summary = "Get Current User Details",
            description = "Returns profile and role data for the authenticated user."
    )
    public ResponseEntity<UserInfoResponse> getCurrentUserDetails(Authentication authentication) {
        UserInfoResponse response = authService.getCurrentUserDetails(authentication);
        return ResponseEntity.ok().body(response);
    }

}
