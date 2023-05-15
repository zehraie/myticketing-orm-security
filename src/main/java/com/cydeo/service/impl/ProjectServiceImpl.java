package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
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
        projectRepository.save(project);

    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.OPEN);
        projectRepository.save(project);
    }
}
