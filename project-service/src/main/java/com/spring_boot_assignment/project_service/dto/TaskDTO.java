package com.spring_boot_assignment.project_service.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private int id;
    private String name;
    private String taskStatus;
    private String assignedTo;
    private int projectId;
}
