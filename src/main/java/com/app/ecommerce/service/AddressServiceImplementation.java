package com.app.ecommerce.service;

import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.Address;
import com.app.ecommerce.model.User;
import com.app.ecommerce.payload.AddressDTO;
import com.app.ecommerce.repository.AddressRepository;
import com.app.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImplementation implements AddressService {

    private final AddressRepository addressRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {

        User user = authUtil.loggedInUser();

        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> fetchAllAdresses() {
        List<Address> addressList = addressRepository.findAll();
        if (addressList.isEmpty()) {
            throw new APIException("No Address Found!");
        }

        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address",  "addressId", addressId));
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        return addressDTO;
    }
}
