package com.tasks_service.controller;


import com.tasks_service.entity.Tasks;
import com.tasks_service.service.TasksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TasksControllerTest {

    @Mock
    private TasksService tasksService;

    @InjectMocks
    private TasksController tasksController;

    private Tasks task;

    @BeforeEach
    public void setUp() {
        task = new Tasks();
        task.setId(1);
        task.setName("Sample Task");
    }

    @Test
    public void getAllTasks_ShouldReturnListOfTasks() {
        List<Tasks> tasks = new ArrayList<>();
        tasks.add(task);

        when(tasksService.findAll()).thenReturn(tasks);

        List<Tasks> result = tasksController.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(tasksService, times(1)).findAll();
    }

    @Test
    public void getTasksById_ExistingId_ShouldReturnTask() {
        when(tasksService.findById(1)).thenReturn(task);

        Tasks result = tasksController.getTasksById(1);

        assertNotNull(result);
        assertEquals(task, result);
        verify(tasksService, times(1)).findById(1);
    }

    @Test
    public void saveTasks_ShouldReturnSavedTask() {
        when(tasksService.save(any(Tasks.class))).thenReturn(task);

        Tasks result = tasksController.saveTasks(task);

        assertNotNull(result);
        assertEquals(task, result);
        verify(tasksService, times(1)).save(task);
    }

    @Test
    public void updateTasks_ShouldReturnUpdatedTask() {
        when(tasksService.update(eq(1), any(Tasks.class))).thenReturn(task);

        Tasks result = tasksController.updateTasks(1, task);

        assertNotNull(result);
        assertEquals(task, result);
        verify(tasksService, times(1)).update(eq(1), any(Tasks.class));
    }

    @Test
    public void deleteTasks_ShouldReturnDeletionMessage() {
        String expectedMessage = "Deleted task with id 1 successfully";
        when(tasksService.delete(1)).thenReturn(expectedMessage);

        String result = tasksController.deleteTasks(1);

        assertEquals(expectedMessage, result);
        verify(tasksService, times(1)).delete(1);
    }

    @Test
    public void getTasksByProjectId_ShouldReturnListOfTasks() {
        List<Tasks> tasks = new ArrayList<>();
        tasks.add(task);

        when(tasksService.findByProjectId(1)).thenReturn(tasks);

        List<Tasks> result = tasksController.getTasksByProjectId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(tasksService, times(1)).findByProjectId(1);
    }

    @Test
    public void deleteTasksByProjectId_ShouldCallService() {
        doNothing().when(tasksService).deleteByProjectId(1);

        tasksController.deleteTasksByProjectId(1);

        verify(tasksService, times(1)).deleteByProjectId(1);
    }
}

