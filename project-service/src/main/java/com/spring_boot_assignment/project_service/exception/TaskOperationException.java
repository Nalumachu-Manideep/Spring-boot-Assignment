package com.spring_boot_assignment.project_service.exception;

public class TaskOperationException extends RuntimeException {
    public TaskOperationException(String message) {
        super(message);
    }
}
