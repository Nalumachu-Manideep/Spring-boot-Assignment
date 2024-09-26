package com.tasks_service.service;


import com.tasks_service.exception.TaskNotFoundException;
import com.tasks_service.exception.TaskOperationException;
import com.tasks_service.entity.Tasks;
import com.tasks_service.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TasksServiceTest {

    @Mock
    private TasksRepository tasksRepository;

    @InjectMocks
    private TasksServiceImpl tasksService;

    private Tasks task;

    @BeforeEach
    public void setUp() {
        task = new Tasks();
        task.setId(1);
        task.setName("Sample Task");
        task.setTaskStatus("Pending");
        task.setAssignedTo("User A");
    }

    @Test
    public void findAll_ShouldReturnListOfTasks() {
        List<Tasks> tasks = new ArrayList<>();
        tasks.add(task);

        when(tasksRepository.findAll()).thenReturn(tasks);

        List<Tasks> result = tasksService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(tasksRepository, times(1)).findAll();
    }

    @Test
    public void findById_ExistingId_ShouldReturnTask() {
        when(tasksRepository.findById(1)).thenReturn(Optional.of(task));

        Tasks result = tasksService.findById(1);

        assertNotNull(result);
        assertEquals(task, result);
        verify(tasksRepository, times(1)).findById(1);
    }

    @Test
    public void findById_NonExistingId_ShouldThrowTaskNotFoundException() {
        when(tasksRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> tasksService.findById(1));
        verify(tasksRepository, times(1)).findById(1);
    }

    @Test
    public void save_ShouldReturnSavedTask() {
        when(tasksRepository.save(any(Tasks.class))).thenReturn(task);

        Tasks result = tasksService.save(task);

        assertNotNull(result);
        assertEquals(task, result);
        verify(tasksRepository, times(1)).save(task);
    }

    @Test
    public void update_ExistingId_ShouldReturnUpdatedTask() {
        when(tasksRepository.findById(1)).thenReturn(Optional.of(task));
        when(tasksRepository.save(any(Tasks.class))).thenReturn(task);

        task.setName("Updated Task");
        Tasks updatedTask = tasksService.update(1, task);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getName());
        verify(tasksRepository, times(1)).findById(1);
        verify(tasksRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    public void update_NonExistingId_ShouldThrowTaskNotFoundException() {
        when(tasksRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> tasksService.update(1, task));
        verify(tasksRepository, times(1)).findById(1);
    }

    @Test
    public void delete_ExistingId_ShouldReturnDeletionMessage() {
        when(tasksRepository.findById(1)).thenReturn(Optional.of(task));

        String result = tasksService.delete(1);

        assertEquals("Deleted the Tasks with id 1 successfully", result);
        verify(tasksRepository, times(1)).findById(1);
        verify(tasksRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_NonExistingId_ShouldThrowTaskOperationException() {
        when(tasksRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskOperationException.class, () -> tasksService.delete(1));
        verify(tasksRepository, times(1)).findById(1);
    }

    @Test
    public void findByProjectId_ShouldReturnListOfTasks() {
        List<Tasks> tasks = new ArrayList<>();
        tasks.add(task);

        when(tasksRepository.findByProjectId(1)).thenReturn(tasks);

        List<Tasks> result = tasksService.findByProjectId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(tasksRepository, times(1)).findByProjectId(1);
    }

    @Test
    public void deleteByProjectId_ShouldCallRepository() {
        doNothing().when(tasksRepository).deleteByProjectId(1);

        tasksService.deleteByProjectId(1);

        verify(tasksRepository, times(1)).deleteByProjectId(1);
    }
}

