package com.tasks_service.controller;


import com.tasks_service.entity.Tasks;
import com.tasks_service.service.TasksService;
import com.tasks_service.utils.Constants;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.TASKS_BASE_URL)
@RequiredArgsConstructor
public class TasksController {
    private final TasksService tasksService;

    @GetMapping
    public List<Tasks> getAllTasks(){
        return tasksService.findAll();
    }
    @GetMapping(Constants.TASK_ID)
    public Tasks getTasksById(@PathVariable int id){
        Tasks Tasks=tasksService.findById(id);
        return Tasks;
    }
    @PostMapping
    public Tasks saveTasks(@RequestBody Tasks Tasks){
        tasksService.save(Tasks);
        return Tasks;
    }

    @PutMapping(Constants.TASK_ID)
    public Tasks updateTasks(@PathVariable int id,@RequestBody Tasks tasks){
        tasksService.update(id,tasks);

        return tasks;
    }
    @DeleteMapping(Constants.TASK_ID)
    public String deleteTasks(@PathVariable int id){
        return tasksService.delete(id);
    }

    @GetMapping(Constants.TASKS_BY_PROJECT)
    public List<Tasks> getTasksByProjectId(@PathVariable int projectId){
        return tasksService.findByProjectId(projectId);
    }

    @DeleteMapping(Constants.TASKS_BY_PROJECT)
    public void deleteTasksByProjectId(@PathVariable int projectId){
        tasksService.deleteByProjectId(projectId);
    }
}

