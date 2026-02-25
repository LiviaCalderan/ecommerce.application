package com.app.ecommerce.controller;

import com.app.ecommerce.payload.AddressDTO;
import com.app.ecommerce.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Address", description = "Address related operations")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/addresses")
    @Operation(
            summary = "Add New Address",
            description = "Creates a new address for the authenticated user."
    )
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);

    }

    @GetMapping("/addresses")
    @Operation(
            summary = "Get All Addresses",
            description = "Returns all registered addresses."
    )
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> allAddressesDTO = addressService.fetchAllAdresses();
        return new ResponseEntity<>(allAddressesDTO, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    @Operation(
            summary = "Get Address By ID",
            description = "Returns the address that matches the informed id."
    )
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressesDTO = addressService.fetchAddressById(addressId);
        return new ResponseEntity<>(addressesDTO, HttpStatus.OK);
    }

    @GetMapping("/addresses/users/address")
    @Operation(
            summary = "Get Current User Addresses",
            description = "Returns all addresses associated with the authenticated user."
    )
    public ResponseEntity<List<AddressDTO>> getAllAddressesByUser() {
        List<AddressDTO> allAddressesDTO = addressService.fetchAddressesByUser();
        return new ResponseEntity<>(allAddressesDTO, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    @Operation(
            summary = "Update Address By ID",
            description = "Updates the address fields for the informed id."
    )
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,
                                                        @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddressDTO = addressService.updateAddressById(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(
            summary = "Delete Address",
            description = "Deletes the address identified by the informed id."
    )
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.NO_CONTENT);

    }
}
