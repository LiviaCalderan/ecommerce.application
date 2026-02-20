package com.app.ecommerce.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@Data
public class UserInfoResponse  {

    private UUID id;
    private String username;
    private List<String> roles;

    public UserInfoResponse(UUID id, String username, List<String> roles ) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

}
