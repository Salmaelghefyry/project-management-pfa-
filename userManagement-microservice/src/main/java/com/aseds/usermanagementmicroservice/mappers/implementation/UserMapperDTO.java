package com.aseds.usermanagementmicroservice.mappers.implementation;

import com.aseds.usermanagementmicroservice.mappers.Mapper;
import com.aseds.usermanagementmicroservice.model.AbstractUser;
import com.aseds.usermanagementmicroservice.model.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperDTO implements Mapper<UserDTO, AbstractUser> {
    private final ModelMapper modelMapper;

    public UserMapperDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO mapTo(AbstractUser userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }
    @Override
    public AbstractUser mapFrom(UserDTO userDTO) {
        return modelMapper.map(userDTO, AbstractUser.class);
    }
}
