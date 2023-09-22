package com.huancoder.market.service.serviceImpl;

import com.huancoder.market.dto.AddressDto;
import com.huancoder.market.model.Address;
import com.huancoder.market.repository.AddressRepository;
import com.huancoder.market.security.MyUserDetailService;
import com.huancoder.market.service.IAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    MyUserDetailService userService;
    @Autowired
    AddressRepository repository;
    @Override
    public void deleteAddress(long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Address don't exist!");
        repository.deleteById(id);

    }

    @Override
    public AddressDto updateAddress(AddressDto updateAddress, long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Address don't exist!");
        Address address=repository.findById(id).orElseThrow();
        address.setMain(updateAddress.isMain());
        address.setContent(updateAddress.getContent());
        address.setName(updateAddress.getName());
        var updatedAddress=repository.save(address);
        return mapper.map(updatedAddress, AddressDto.class);
    }

    @Override
    public AddressDto addAddress(AddressDto newAddress) {
        Address address=mapper.map(newAddress,Address.class);
        address.setId(null);
        address.setUser(userService.getCurrentUser());
        var addressAdded=repository.save(address);
        return mapper.map(addressAdded, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddresses() {
        List<Address> address;
        try {
             address = repository.findByUserId(userService.getCurrentUser().getId());
        }catch (Exception e){
             address=new ArrayList<>();
        }
        return address.stream().map(a->mapper.map(a, AddressDto.class)).collect(Collectors.toList());
    }

    @Override
    public AddressDto findById(Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Address don't exist!");
        Address address=repository.findById(id).orElseThrow();
        return mapper.map(address, AddressDto.class);
    }


}
