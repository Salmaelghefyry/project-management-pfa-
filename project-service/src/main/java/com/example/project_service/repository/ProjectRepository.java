package com.example.project_service.repository;

import com.example.project_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProjectManagerId(Long projectManagerId);
    List<Project> findByEmployeeIdsContaining(Long employeeId);
    boolean existsByName(String name);
} 