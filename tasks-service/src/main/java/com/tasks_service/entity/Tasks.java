package com.tasks_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "assigned_to")
    private String assignedTo;
}

