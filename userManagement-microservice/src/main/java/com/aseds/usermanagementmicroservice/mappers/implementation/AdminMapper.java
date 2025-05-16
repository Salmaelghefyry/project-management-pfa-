package com.aseds.usermanagementmicroservice.mappers.implementation;

import com.aseds.usermanagementmicroservice.mappers.Mapper;
import com.aseds.usermanagementmicroservice.model.AdminEntity;
import com.aseds.usermanagementmicroservice.model.dto.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper implements Mapper<RegisterRequest, AdminEntity> {

    private final ModelMapper modelMapper ;

    @Autowired
    public AdminMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RegisterRequest mapTo(AdminEntity adminEntity) {
        return modelMapper.map(adminEntity, RegisterRequest.class);
    }

    @Override
    public AdminEntity mapFrom(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, AdminEntity.class);
    }
}
