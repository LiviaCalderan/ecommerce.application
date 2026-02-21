package com.app.ecommerce.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    String cookie;
    UserInfoResponse userInfoResponse;

}
