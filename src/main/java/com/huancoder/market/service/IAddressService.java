package com.huancoder.market.service;

import com.huancoder.market.dto.AddressDto;

public interface IAddressService {
    void deleteAddress(long id);

    AddressDto updateAddress(AddressDto updateAddress, long id);

    AddressDto addAddress(AddressDto newAddress);

    Object getAddresses();

    AddressDto findById(Long id);
}
