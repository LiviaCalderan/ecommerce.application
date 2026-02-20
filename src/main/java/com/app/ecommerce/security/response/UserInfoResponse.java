package com.app.ecommerce.security.response;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@NoArgsConstructor
public class UserInfoResponse  {

    private UUID id;
    private String jwtToken;
    private String username;
    private List<String> roles;

    public UserInfoResponse(UUID id, String username, List<String> roles, String jwtToken) {
    }
}
