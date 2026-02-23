package com.app.ecommerce.util;

import com.app.ecommerce.model.User;
import com.app.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String loggedInEmail() {
        User user = loggedInUser();
        return user.getEmail();
    }

    public UUID loggedInUserId() {
        User user = loggedInUser();
        return user.getUserId();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found!"));
        return user;
    }
}
