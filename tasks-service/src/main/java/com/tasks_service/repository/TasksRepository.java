package com.tasks_service.repository;
import java.util.*;
import com.tasks_service.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks,Integer> {
     List<Tasks> findByProjectId(int projectId);
     void deleteByProjectId(int projectId);
}
