package com.aseds.usermanagementmicroservice.mappers.implementation;

import com.aseds.usermanagementmicroservice.mappers.Mapper;
import com.aseds.usermanagementmicroservice.model.EmployeeEntity;
import com.aseds.usermanagementmicroservice.model.dto.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements Mapper<RegisterRequest,EmployeeEntity> {
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RegisterRequest mapTo(EmployeeEntity employeeEntity) {
        return modelMapper.map(employeeEntity, RegisterRequest.class);
    }

    @Override
    public EmployeeEntity mapFrom(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, EmployeeEntity.class);
    }
}
