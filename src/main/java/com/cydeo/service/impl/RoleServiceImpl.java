package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.mapperUtil = mapperUtil;
    }


    @Override
    public List<RoleDTO> listAllRoles() {
        //controller calling me and requesting all the roles
        //so... I need to go to database and bring all the roles from there
        // to call repo we need injection above  and provide data to controller
        // bring all the role for controller
        List<Role> roleList =roleRepository.findAll();
        // I need a mekanism to convert entity to dto  bu beni MAPPER gorurecak
        //convert entity to dto-Mapper-get roles from db and convert each role to roledto

//       List<RoleDTO>list2= roleList.stream().map(roleMapper::convertToDto)
//                .collect(Collectors.toList());//aynis obj-> roleMapper.convertToDto(obj)
//        return list2;
        //or
        //return roleList.stream().map(roleMapper::convertToDto).collect(Collectors.toList());
        // return  roleRepository.findAll().stream().map(roleMapper::convertToDto)
        //                .collect(Collectors.toList());

        // MapperUtil implementation
                                                                           // convert role entity object to new RoleDTO object
        return roleRepository.findAll().stream().map(role ->mapperUtil.convert(role,new RoleDTO())).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(Long id) {
       // return roleMapper.convertToDto(roleRepository.findById(id).get());//Optional old
        //get() kullandik sonunda
        return  mapperUtil.convert(roleRepository.findById(id).get(),new RoleDTO());
    }
}
