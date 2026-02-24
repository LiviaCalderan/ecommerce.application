package com.app.ecommerce.service;

import com.app.ecommerce.payload.AddressDTO;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO);
}
