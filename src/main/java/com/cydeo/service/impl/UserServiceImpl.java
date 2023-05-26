package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional  userRepository ye koydugumuz icin buraya koymadik bu annotation i
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ProjectService projectService, TaskService taskService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<UserDTO> listAllUsers() { // what is rewponsibility of serviceImp :bring the data from databaser to controller
        List<User> userList = userRepository.findAll(Sort.by("firstName")); //UNUTMA findAll() JPQL repositoryden geliyor
        return userList.stream().map(obj -> userMapper.convertToDTO(obj)).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return userMapper.convertToDTO(user);
    }

    @Override
    // encoded formata cevirecegimizden asagidaki comment off olan ni kullanamayiz
    // database de password Abc1 olarak kaydedilmesin diye save  olurken password da encode yapiyoruz
    public void save(UserDTO dto) {
        dto.setEnabled(true); // this field UIdan gelmiyecegi icin onu save yaparken set yapmaliyiz, , user is active
        User obj = userMapper.convertToEntity(dto);
        obj.setPassWord(passwordEncoder.encode(obj.getPassWord()));
        userRepository.save(obj);
       // userRepository.save(userMapper.convertToEntity(dto));
    }

    @Override
    public UserDTO update(UserDTO dto) { // dto: updated user
        //find current user
        User user = userRepository.findByUserName(dto.getUserName());
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
        User user = userRepository.findByUserName(username);
        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            //in order to use the name again set the name
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }

    }

    private boolean checkIfUserCanBeDeleted(User user) {
        // if the user is not Manager or Employee you can delete so return true
        switch (user.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.readAllByAssignedManager(user);
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.readAllByAssignedEmployee(user);
                return taskDTOList.size() == 0;
            default:
                return true;

        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        //database de role e dayali bana data getirecke metod yok onu yazacagim
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }
}
