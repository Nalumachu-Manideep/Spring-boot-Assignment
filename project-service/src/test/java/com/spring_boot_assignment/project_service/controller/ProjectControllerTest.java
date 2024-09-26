package com.spring_boot_assignment.project_service.controller;

import com.spring_boot_assignment.project_service.dto.TaskDTO;
import com.spring_boot_assignment.project_service.model.Project;
import com.spring_boot_assignment.project_service.service.ProjectService;
import com.spring_boot_assignment.project_service.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private Project project;
    private TaskDTO taskDTO;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1);
        project.setName("Sample Project");

        taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setName("Sample Task");
    }

    @Test
    public void getAllProjects_ShouldReturnListOfProjects() {
        List<Project> projects = new ArrayList<>();
        projects.add(project);

        when(projectService.findAll()).thenReturn(projects);

        List<Project> result = projectController.getAllProjects();

        assertEquals(1, result.size());
        assertEquals("Sample Project", result.get(0).getName());
        verify(projectService, times(1)).findAll();
    }

    @Test
    public void getProjectById_ShouldReturnProject() {
        when(projectService.findById(1)).thenReturn(project);

        Project result = projectController.getProjectById(1);

        assertEquals("Sample Project", result.getName());
        verify(projectService, times(1)).findById(1);
    }

    @Test
    public void saveProject_ShouldReturnSavedProject() {
        when(projectService.save(any(Project.class))).thenReturn(project);

        Project result = projectController.saveProject(project);

        assertEquals("Sample Project", result.getName());
        verify(projectService, times(1)).save(any(Project.class));
    }

    @Test
    public void updateProject_ShouldReturnUpdatedProject() {
        when(projectService.save(any(Project.class))).thenReturn(project);

        Project result = projectController.updateProject(project);

        assertEquals("Sample Project", result.getName());
        verify(projectService, times(1)).save(any(Project.class));
    }

    @Test
    public void deleteProject_ShouldReturnConfirmationMessage() {
        when(projectService.delete(1)).thenReturn("Project deleted");

        String result = projectController.deleteProject(1);

        assertEquals("Project deleted", result);
        verify(projectService, times(1)).delete(1);
    }

    @Test
    public void getTasksForProject_ShouldReturnListOfTasks() {
        List<TaskDTO> tasks = new ArrayList<>();
        tasks.add(taskDTO);

        when(projectService.getTasksForProject(1)).thenReturn(tasks);

        List<TaskDTO> result = projectController.getTasksForProject(1);

        assertEquals(1, result.size());
        assertEquals("Sample Task", result.get(0).getName());
        verify(projectService, times(1)).getTasksForProject(1);
    }

    @Test
    public void createTaskForProject_ShouldReturnCreatedTask() {
        when(projectService.createTaskForProject(eq(1), any(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO result = projectController.createTaskForProject(1, taskDTO);

        assertEquals("Sample Task", result.getName());
        verify(projectService, times(1)).createTaskForProject(eq(1), any(TaskDTO.class));
    }

    @Test
    public void updateTaskForProject_ShouldReturnUpdatedTask() {
        when(projectService.updateTaskForProject(eq(1), eq(1), any(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO result = projectController.updateTaskForProject(1, 1, taskDTO);

        assertEquals("Sample Task", result.getName());
        verify(projectService, times(1)).updateTaskForProject(eq(1), eq(1), any(TaskDTO.class));
    }

    @Test
    public void deleteTaskForProject_ShouldReturnConfirmationMessage() {
        when(projectService.deleteTaskForProject(1, 1)).thenReturn("Task deleted");

        String result = projectController.deleteTaskForProject(1, 1);

        assertEquals("Task deleted", result);
        verify(projectService, times(1)).deleteTaskForProject(1, 1);
    }
}
