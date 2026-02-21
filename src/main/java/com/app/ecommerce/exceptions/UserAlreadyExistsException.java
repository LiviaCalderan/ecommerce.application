package com.app.ecommerce.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException{

    String resourceName;


    public UserAlreadyExistsException(String resourceName){
        super(String.format("This %s is already taken!", resourceName));
        this.resourceName = resourceName;
    }
}
