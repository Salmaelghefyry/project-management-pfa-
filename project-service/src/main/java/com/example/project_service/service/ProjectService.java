package com.example.project_service.service;

import com.example.project_service.dto.AddEmployeeRequest;
import com.example.project_service.dto.CreateProjectRequest;
import com.example.project_service.model.Project;
import com.example.project_service.model.ProjectInfo;
import com.example.project_service.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public Project createProject(CreateProjectRequest request) {
        if (projectRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Project with this name already exists");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setProjectManagerId(request.getProjectManagerId());

        return projectRepository.save(project);
    }

    @Transactional
    public Project addEmployeesToProject(Long projectId, AddEmployeeRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Set<Long> currentEmployees = project.getEmployeeIds();
        currentEmployees.addAll(request.getEmployeeIds());
        project.setEmployeeIds(currentEmployees);

        return projectRepository.save(project);
    }

    public List<Project> getProjectsByManager(Long projectManagerId) {
        return projectRepository.findByProjectManagerId(projectManagerId);
    }

    public List<Project> getProjectsByEmployee(Long employeeId) {
        return projectRepository.findByEmployeeIdsContaining(employeeId);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }



} 