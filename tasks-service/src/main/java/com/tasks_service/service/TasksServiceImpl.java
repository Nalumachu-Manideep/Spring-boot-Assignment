package com.tasks_service.service;

import com.tasks_service.exception.TaskNotFoundException;
import com.tasks_service.exception.TaskOperationException;
import com.tasks_service.entity.Tasks;
import com.tasks_service.repository.TasksRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {
    private final TasksRepository tasksRepository;

    @Override
    public List<Tasks> findAll() {
        return tasksRepository.findAll();
    }

    @Override
    public Tasks findById(int id) {
        Optional<Tasks> Tasks =tasksRepository.findById(id);
        if(Tasks.isPresent()){
            return Tasks.get();
        }
        else{
            throw new TaskNotFoundException("Tasks Not Found");
        }
    }

    @Override
    public Tasks save(Tasks Tasks) {
        return tasksRepository.save(Tasks);
    }

    @Override
    public Tasks update(int id, Tasks tasks) {
        return tasksRepository.findById(id)
                .map(task ->{
                    task.setTaskStatus(tasks.getTaskStatus());
                    task.setName(tasks.getName());
                    task.setAssignedTo(tasks.getAssignedTo());
                    return tasksRepository.save(task);
                }).orElseThrow(()->new TaskNotFoundException("Tasks Not Found"));

    }

    @Override
    public String delete(int id) {
        Optional<Tasks> Tasks=tasksRepository.findById(id);
        if(Tasks.isPresent()){
            tasksRepository.deleteById(id);
        }else{
            throw new TaskOperationException("Tasks Not Found "+id);
        }
        return "Deleted the Tasks with id "+id+" successfully";
    }

    @Override
    public List<Tasks> findByProjectId(int projectId) {
        return tasksRepository.findByProjectId(projectId);
    }
    @Transactional
    @Override
    public void deleteByProjectId(int projectId) {
        tasksRepository.deleteByProjectId(projectId);
    }
}
