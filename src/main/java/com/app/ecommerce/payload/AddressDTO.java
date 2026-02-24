package com.app.ecommerce.payload;

import com.app.ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long addressId;
    private String street;
    private String buildingName;
    private String number;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private List<User> users = new ArrayList<>();
}
