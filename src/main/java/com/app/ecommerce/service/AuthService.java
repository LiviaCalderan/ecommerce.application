package com.app.ecommerce.service;

import com.app.ecommerce.security.request.LoginRequest;
import com.app.ecommerce.security.request.SignupRequest;
import com.app.ecommerce.security.response.AuthResponse;
import com.app.ecommerce.security.response.UserInfoResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthResponse signinUser(LoginRequest loginRequest);

    void signupUser(SignupRequest signupRequest);

    String signout();

    UserInfoResponse getCurrentUserDetails(Authentication authentication);
}
