package com.spring_boot_assignment.project_service.controller;

import com.spring_boot_assignment.project_service.dto.TaskDTO;
import com.spring_boot_assignment.project_service.service.ProjectService;
import com.spring_boot_assignment.project_service.utils.Constants;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import com.spring_boot_assignment.project_service.model.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.PRODUCT_BASE_URL)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public List<Project> getAllProjects(){
        return projectService.findAll();
    }
    @GetMapping(Constants.PRODUCT_ID)
    public Project getProjectById(@PathVariable int id){
        Project project=projectService.findById(id);
        return project;
    }
    @PostMapping
    public Project saveProject(@RequestBody Project project){
        projectService.save(project);
        return project;
    }

    @PutMapping
    public Project updateProject(@RequestBody Project project){
        projectService.save(project);
        return project;
    }
    @DeleteMapping(Constants.PRODUCT_ID)
    public String deleteProject(@PathVariable int id){
        return projectService.delete(id);
    }

    @GetMapping(Constants.PRODUCT_ID_TASKS)
    public List<TaskDTO> getTasksForProject(@PathVariable int projectId){
        return projectService.getTasksForProject(projectId);
    }

    @PostMapping(Constants.PRODUCT_ID_TASKS)
    public TaskDTO createTaskForProject(@PathVariable int projectId,@RequestBody TaskDTO taskDTO){
        return projectService.createTaskForProject(projectId,taskDTO);
    }

    @PutMapping(Constants.PRODUCT_ID_TASKS_ID)
    public TaskDTO updateTaskForProject(@PathVariable int projectId,@PathVariable int taskId,@RequestBody TaskDTO taskDTO){
        return projectService.updateTaskForProject(projectId,taskId,taskDTO);
    }

    @DeleteMapping(Constants.PRODUCT_ID_TASKS_ID)
    public String deleteTaskForProject(@PathVariable int projectId,@PathVariable int taskId){
        return projectService.deleteTaskForProject(projectId,taskId);
    }
}
