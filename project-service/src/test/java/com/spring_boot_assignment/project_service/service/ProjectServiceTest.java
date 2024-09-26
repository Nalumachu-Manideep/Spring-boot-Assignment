package com.spring_boot_assignment.project_service.service;


import com.spring_boot_assignment.project_service.dto.TaskDTO;
import com.spring_boot_assignment.project_service.model.Project;
import com.spring_boot_assignment.project_service.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1);
        project.setName("Test Project");
    }

    @Test
    public void findAll_ShouldReturnAllProjects() {
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));

        var projects = projectService.findAll();

        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void findById_ExistingId_ShouldReturnProject() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project foundProject = projectService.findById(1);

        assertEquals(project, foundProject);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    public void findById_NonExistingId_ShouldThrowException() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.findById(1));
        assertEquals("Project Not Found", exception.getMessage());
    }

    @Test
    public void save_ShouldReturnSavedProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project savedProject = projectService.save(project);

        assertEquals(project, savedProject);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void delete_ExistingId_ShouldDeleteProject() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        String result = projectService.delete(1);

        assertEquals("Deleted the Project with id 1successfully", result);
        verify(restTemplate, times(1)).delete(any(String.class));
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_NonExistingId_ShouldThrowException() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.delete(1));
        assertEquals("Project Not Found 1", exception.getMessage());
    }

    @Test
    public void getTasksForProject_ExistingProjectId_ShouldReturnTaskList() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        ResponseEntity<List<TaskDTO>> responseEntity = ResponseEntity.ok(Collections.emptyList());
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(new ParameterizedTypeReference<List<TaskDTO>>() {})))
                .thenReturn(responseEntity);

        List<TaskDTO> tasks = projectService.getTasksForProject(1);

        assertNotNull(tasks);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(new ParameterizedTypeReference<List<TaskDTO>>() {}));
    }

    @Test
    public void getTasksForProject_NonExistingProjectId_ShouldThrowException() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.getTasksForProject(1));
        assertEquals("Project Not Found", exception.getMessage());
    }

    @Test
    public void createTaskForProject_ExistingProjectId_ShouldReturnTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setName("Test Task");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(restTemplate.postForEntity(anyString(), any(TaskDTO.class), eq(TaskDTO.class)))
                .thenReturn(ResponseEntity.ok(taskDTO));

        TaskDTO createdTask = projectService.createTaskForProject(1, taskDTO);

        assertNotNull(createdTask);
        assertEquals(taskDTO, createdTask);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(TaskDTO.class), eq(TaskDTO.class));
    }

    @Test
    public void createTaskForProject_NonExistingProjectId_ShouldThrowException() {
        TaskDTO taskDTO = new TaskDTO();

        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.createTaskForProject(1, taskDTO));
        assertEquals("Project Not Found", exception.getMessage());
    }

    @Test
    public void updateTaskForProject_ExistingProjectAndTaskId_ShouldReturnUpdatedTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setName("Updated Task");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(TaskDTO.class)))
                .thenReturn(ResponseEntity.ok(taskDTO));

        TaskDTO updatedTask = projectService.updateTaskForProject(1, 1, taskDTO);

        assertNotNull(updatedTask);
        assertEquals(taskDTO, updatedTask);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(TaskDTO.class));
    }

    @Test
    public void updateTaskForProject_NonExistingProject_ShouldThrowException() {
        TaskDTO taskDTO = new TaskDTO();

        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.updateTaskForProject(1, 1, taskDTO));
        assertEquals("Project Not Found", exception.getMessage());
    }

    @Test
    public void deleteTaskForProject_ExistingProjectAndTaskId_ShouldReturnSuccessMessage() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        String result = projectService.deleteTaskForProject(1, 1);

        assertEquals("Deleted the task of project of id 1 with task id 1 successfully", result);
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    public void deleteTaskForProject_NonExistingProject_ShouldThrowException() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.deleteTaskForProject(1, 1));
        assertEquals("Project Not found", exception.getMessage());
    }
}

