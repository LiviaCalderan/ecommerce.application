package com.app.ecommerce.service;

import com.app.ecommerce.payload.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> fetchAllAdresses();

    AddressDTO fetchAddressById(Long addressId);

    List<AddressDTO> fetchAddressesByUser();
}
