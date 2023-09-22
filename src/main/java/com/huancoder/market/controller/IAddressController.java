package com.huancoder.market.controller;

import com.huancoder.market.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/addresses")
public interface IAddressController {
    @GetMapping("")
    public ResponseEntity<?> getAddresses();
    @PostMapping("")
    public ResponseEntity<?> addNewAddress(@RequestBody AddressDto newAddress);
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@RequestBody AddressDto updateAddress, @PathVariable("id") final long id);
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") final long id);
    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable("id") final long id);

}
