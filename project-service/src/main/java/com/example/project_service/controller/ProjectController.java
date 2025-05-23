package com.example.project_service.controller;

import com.example.project_service.model.ProjectInfo;
import com.example.project_service.model.ProjetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjetRepository repository ;

    public ProjectController(ProjetRepository repository) {

        this.repository = repository;
    }

    @PostMapping("/project-info/save")
    public List<ProjectInfo> saveAll(@RequestBody List<ProjectInfo> projectInfoList) {
        return repository.saveAll(projectInfoList);
    }
    public ProjectInfo save(@RequestBody ProjectInfo projectInfo) {
        return repository.save(projectInfo);
    }

    @GetMapping("/project-info/list")
    public List<ProjectInfo> getAll() {
        return repository.findAll();
    }

}