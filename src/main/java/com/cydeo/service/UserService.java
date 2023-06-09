package com.cydeo.service;

import com.cydeo.dto.UserDTO;


import java.util.List;

public interface UserService  {
List<UserDTO> listAllUsers();
UserDTO findByUserName(String username);
void save(UserDTO dto);
UserDTO update(UserDTO dto);
void deletebyUserName(String username);
void delete(String username);

//project page icin available manager lari gormem gerekiyor o yuzden
    //fakat diger sayfada da Employeee lazim olacak o yuzden generic yapiyoruz
    List<UserDTO> listAllByRole(String role);

}
