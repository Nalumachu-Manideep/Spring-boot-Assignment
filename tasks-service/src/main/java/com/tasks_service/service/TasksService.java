package com.tasks_service.service;

import com.tasks_service.entity.Tasks;

import java.util.List;

public interface TasksService {
    public List<Tasks> findAll();
    public Tasks findById(int id);
    public Tasks save(Tasks Tasks);
    public Tasks update(int id,Tasks tasks);
    public String delete(int id);
    List<Tasks> findByProjectId(int projectId);
    public void deleteByProjectId(int projectId);
}
