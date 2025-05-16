package com.aseds.usermanagementmicroservice.mappers.implementation;

import com.aseds.usermanagementmicroservice.mappers.Mapper;
import com.aseds.usermanagementmicroservice.model.ProjectManagerEntity;
import com.aseds.usermanagementmicroservice.model.dto.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectManagerMapper implements Mapper<RegisterRequest, ProjectManagerEntity> {
    private final ModelMapper modelMapper ;

    @Autowired
    public ProjectManagerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RegisterRequest mapTo(ProjectManagerEntity projectManagerEntity) {
        return modelMapper.map(projectManagerEntity, RegisterRequest.class);
    }
    @Override
    public ProjectManagerEntity mapFrom(RegisterRequest userDTO) {
        return modelMapper.map(userDTO, ProjectManagerEntity.class);
    }
}
