package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.IAddressController;
import com.huancoder.market.controller.ICouponController;
import com.huancoder.market.dto.AddressDto;
import com.huancoder.market.dto.CouponDto;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AddressControllerImpl implements IAddressController {
    @Autowired
    IAddressService addressService;
    @Override
    public ResponseEntity<?> getAddresses() {
        List<AddressDto> dtoList= (List<AddressDto>) addressService.getAddresses();
        if(dtoList.size()==0)
            return ResponseEntity.ok().body(
                    SuccessResponse.builder()
                            .status(StatusEnum.OK)
                            .message("User haven't address")
                            .data(null)
                            .build()
            );
        List<EntityModel<AddressDto>> entityModels=dtoList.stream()
                .map(ad->{
                    EntityModel<AddressDto> addressDtoEntityModel = EntityModel.of(ad);
                    addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).getAddressById(ad.getId())).withSelfRel());
                    addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(IAddressController.class).withRel("add"));
                    addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo((IAddressController.class)).slash(ad.getId()).withRel("update"));
                    addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).deleteAddress(ad.getId())).withRel("delete"));
                    return addressDtoEntityModel;
                }).collect(Collectors.toList());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("These are addresses of user")
                        .data(entityModels)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> addNewAddress(AddressDto newAddress) {
        AddressDto ad = addressService.addAddress(newAddress);
        EntityModel<AddressDto> addressDtoEntityModel = EntityModel.of(ad);
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).getAddresses()).withRel("all-address"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo((IAddressController.class)).slash(ad.getId()).withRel("update"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).getAddressById(ad.getId())).withRel("get"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).deleteAddress(ad.getId())).withRel("delete"));
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Address are added!")
                        .data(addressDtoEntityModel)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> updateAddress(AddressDto updateAddress, long id) {
        AddressDto ad = addressService.updateAddress(updateAddress, id);
        EntityModel<AddressDto> addressDtoEntityModel = EntityModel.of(ad);
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).getAddressById(ad.getId())).withRel("get"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).deleteAddress(ad.getId())).withRel("delete"));
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Updated Successfully")
                        .data(addressDtoEntityModel)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deleteAddress(long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Deleted Successfully")
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getAddressById(long id) {
        AddressDto ad = addressService.findById(id);
        EntityModel<AddressDto> addressDtoEntityModel = EntityModel.of(ad);
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).getAddresses()).withRel("all-address"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(IAddressController.class).withRel("add"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo((IAddressController.class)).slash(ad.getId()).withRel("update"));
        addressDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IAddressController.class).deleteAddress(ad.getId())).withRel("delete"));
          return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Deleted Successfully")
                        .data(addressDtoEntityModel)
                        .build()
        );
    }
}
