package com.cydeo.service;



import com.cydeo.dto.RoleDTO;

import java.util.List;

public interface RoleService {
   //RoleDTO because we are in service, view work with dto and I will calll from controller
    List<RoleDTO> listAllRoles();
    RoleDTO findById(Long id);
}
