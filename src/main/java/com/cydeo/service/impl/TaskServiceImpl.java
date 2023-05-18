package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    @Override
    public TaskDTO findById(Long id) {

        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return taskMapper.convertToDTO(task.get());
        }
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        // I will get all tasks from dataBase and will show on the UI
        return taskRepository.findAll().stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
   // elinde dto var ve onu database save yapican cevirmne lazim
        //status form ile gelmiyor listede var ama oyuzden onuda ilave ediyoruz,set the status
     dto.setTaskStatus(Status.OPEN);
     //assigned Date de gelmiyor form ile onuda ilave etmeliyiz
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        taskRepository.save(task);
    }
    //bring the database
    //status and assigned date is not coming from DTO(UI da goremiyorum gelmiyor) and I need to capture those from the taskRepository
    // olmayanlari database den alip atiyouruz convertedTask a
    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task =taskRepository.findById(dto.getId()); // bulduk UI aldigimz id ile databaseden getirdik
        Task convertedTask = taskMapper.convertToEntity(dto);
        if(task.isPresent()){
            //otherwise we get dublicate id
            convertedTask.setId(task.get().getId());
            convertedTask.setTaskStatus(task.get().getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }


    }

    @Override
    public void delete(Long id) {
        //I need to bring the table then change the flag
        //findById return Optinal: protect null pointer exception
        Optional<Task> foundTask = taskRepository.findById(id);

        if(foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }

    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }
}
