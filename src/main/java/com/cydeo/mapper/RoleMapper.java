package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    //If i want to use any method from ModelMapper class, What i need to do?
    //InJECTION

    private final ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    //converttoEntity
    public Role convertToEntity(RoleDTO dto){
    return modelMapper.map(dto,Role.class); // target class is Role.class
    }

    //convertToDTO
    public RoleDTO convertToDto(Role entity){
     return modelMapper.map(entity,RoleDTO.class);
    }
}
