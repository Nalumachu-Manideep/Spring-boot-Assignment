package com.spring_boot_assignment.project_service.service;

import com.spring_boot_assignment.project_service.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import com.spring_boot_assignment.project_service.model.Project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.spring_boot_assignment.project_service.repository.ProjectRepository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final RestTemplate restTemplate;
    private final String tasksServiceUrl;

    public ProjectServiceImpl(ProjectRepository projectRepository, RestTemplate restTemplate,
                              @Value("${task.service.url}") String tasksServiceUrl) {
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
        this.tasksServiceUrl = tasksServiceUrl;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(int id) {
        Optional<Project> project =projectRepository.findById(id);
        if(project.isPresent()){
            return project.get();
        }
        else{
            throw new RuntimeException("Project Not Found");
        }
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public String delete(int id) {
        Optional<Project> project=projectRepository.findById(id);
        if(project.isPresent()){
            restTemplate.delete(tasksServiceUrl + "/tasks/project/"+id);
            projectRepository.deleteById(id);
        }else{
            throw new RuntimeException("Project Not Found "+id);
        }
        return "Deleted the Project with id "+id+"successfully";
    }



    @Override
    public  List<TaskDTO> getTasksForProject(int projectId) {
        Project project=projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project Not Found"));
        ResponseEntity<List<TaskDTO>> response=restTemplate.exchange(
                tasksServiceUrl + "/tasks/project/" + projectId,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TaskDTO>>() {
                }
        );
        return response.getBody();
    }

    @Override
    public TaskDTO createTaskForProject(int projectId, TaskDTO taskDTO) {
        Project project=projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException("Project Not Found"));
        taskDTO.setProjectId(projectId);
        ResponseEntity<TaskDTO> response=restTemplate.postForEntity(
                tasksServiceUrl + "/tasks",
                taskDTO,
                TaskDTO.class
        );
        return response.getBody();
    }

    @Override
    public TaskDTO updateTaskForProject(int projectId, int taskId, TaskDTO taskDTO) {
        Project project=projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException("Project Not Found"));

        ResponseEntity<TaskDTO> response=restTemplate.exchange(
                tasksServiceUrl + "/tasks/" + taskId,
                HttpMethod.PUT,
                new HttpEntity<>(taskDTO),
                TaskDTO.class
        );
        return response.getBody();
    }

    @Override
    public String deleteTaskForProject(int projectId, int taskId) {
        Project project=projectRepository.findById(projectId)
                .orElseThrow(()->new RuntimeException("Project Not found"));
        restTemplate.delete(tasksServiceUrl + "/tasks/"+taskId);

        return "Deleted the task of project of id "+projectId+" with task id "+taskId+" successfully";
    }
}
