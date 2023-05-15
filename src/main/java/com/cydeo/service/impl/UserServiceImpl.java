package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional  userRepository ye koydugumuz icin buraya koymadik bu annotation i
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public List<UserDTO> listAllUsers() { // what is rewponsibility of serviceImp :bring the data from databaser to controller
  List<User> userList = userRepository.findAll(Sort.by("firstName")); //UNUTMA findAll() JPQL repositoryden geliyor
        return userList.stream().map(obj-> userMapper.convertToDTO(obj)).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return userMapper.convertToDTO(user);
    }

    @Override
    public void save(UserDTO dto) {
    userRepository.save(userMapper.convertToEntity(dto));
    }

    @Override
    public UserDTO update(UserDTO dto) { // dto: updated user
        //find current user
        User user =userRepository.findByUserName(dto.getUserName());
        //Map updated user dto to entity object
        User convertedUser = userMapper.convertToEntity(dto);
        //set id to converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);
        return findByUserName(dto.getUserName());
    }

    @Override
    public void deletebyUserName(String username) {
  userRepository.deleteByUserName(username);
    }

    @Override
    public void delete(String username) {
       // I am not deleting data in db
        User user= userRepository.findByUserName(username);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        //database de role e dayali bana data getirecke metod yok onu yazacagim
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }
}
