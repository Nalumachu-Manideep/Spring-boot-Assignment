package com.spring_boot_assignment.project_service.repository;

import com.spring_boot_assignment.project_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
}
