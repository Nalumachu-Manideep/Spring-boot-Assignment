package com.spring_boot_assignment.project_service.service;

import com.spring_boot_assignment.project_service.dto.TaskDTO;
import com.spring_boot_assignment.project_service.model.Project;

import java.util.List;

public interface ProjectService {
    public List<Project> findAll();
    public Project findById(int id);
    public Project save(Project project);
    public String delete(int id);
    public List<TaskDTO> getTasksForProject(int projectId);
    public TaskDTO createTaskForProject(int projectId,TaskDTO taskDTO);
    public TaskDTO updateTaskForProject(int projectId,int taskId,TaskDTO taskDTO);
    public String deleteTaskForProject(int projectId,int taskId);
}
