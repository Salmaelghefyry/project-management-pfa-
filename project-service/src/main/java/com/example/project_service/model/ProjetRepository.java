package com.example.project_service.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjetRepository extends JpaRepository<ProjectInfo, Integer> {
    List<ProjectInfo> findByResponsableId(Integer responsableId);
}