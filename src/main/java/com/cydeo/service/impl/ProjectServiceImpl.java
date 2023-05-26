package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, @Lazy UserService userService, UserMapper userMapper, TaskRepository taskRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }


    @Override
    public ProjectDTO getByProjectCode(String code) {
        // kendi custom methodu kullaniyorum
        Project project = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDTO(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
   //findAll() method JPA den geliyor
        List<Project> list = projectRepository.findAll();
        return list.stream().map(obj->projectMapper.convertToDTO(obj))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {
   //Find the project
        // project has id which comes from db
   Project project= projectRepository.findByProjectCode(dto.getProjectCode());
   // convertedProject doesnt have id
   Project convertedProject = projectMapper.convertToEntity(dto);
   convertedProject.setId(project.getId());
   convertedProject.setProjectStatus(project.getProjectStatus());
   projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {
        // bring to database the project
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode()+"-"+project.getId());
        projectRepository.save(project);
        //when we delete the project we have to delete also task
        taskService.deleteByProject(projectMapper.convertToDTO(project));

    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
        taskService.completeByProject(projectMapper.convertToDTO(project));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {// sonunda donusturecegim ProjectDTO
        //harold@manager.com login in the system,when click the project status then see the all project
        //I need to get all project and assign to this manager
        //login ile aliyourz user i asagida
       // UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");
        UserDTO currentUserDTO = userService.findByUserName("mike@gmail.com"); // for manager username, admin@admin.com, passeord Abc1 ile giris yap Abc1 sonra mike smith and Abc1 password olacak sekilde manager create et->logout yap-> sonra bu creadiantial lar ile giris yap->create project navigate yapabilirsin
        User user = userMapper.convertToEntity(currentUserDTO);
        List<Project> list = projectRepository.findAllByAssignedManager(user); //User object entity needs
        return list.stream().map(project->{
            ProjectDTO obj = projectMapper.convertToDTO(project);
            obj.setUnfinishedTaskCounts(taskRepository.totalNonCompletedTasks(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskRepository.totalCompletedTasks(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }
    //list project icinde, hicbitr projede project code ve digerleri assigend manager,  da var database den bakabiliriz
    //ama project-status.html file da unfinishedTask_count yok ve completeTestcount da yok
    //ama ProjectDTO da var   vede taskTable dadavar
    //list projed her birine set yapacagim bu olmayanlari
    @Override
    public List<ProjectDTO> readAllByAssignedManager(User assignedManager) {
        List<Project> list= projectRepository.findAllByAssignedManager(assignedManager);
      return list.stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }


}
