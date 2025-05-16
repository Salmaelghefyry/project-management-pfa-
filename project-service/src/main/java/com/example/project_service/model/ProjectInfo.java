package com.example.project_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "Projet")
public class ProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projet")
    private Integer id;

    @NotBlank
    @Column(name = "titre_projet", nullable = false)
    private String titre;

    @Column(name = "desc_projet")
    private String description;

    @Column(name = "startDate_projet", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "dueDate_projet", nullable = false)
    private LocalDateTime dueDate;

    @NotNull
    @Column(name = "respo_projet", nullable = false)
    private Integer responsableId;

    // Default constructor
    public ProjectInfo() {
    }

    // Constructor with fields
    public ProjectInfo(String titre, String description, LocalDateTime startDate,
                  LocalDateTime dueDate, Integer responsableId) {
        this.titre = titre;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.responsableId = responsableId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(Integer responsableId) {
        this.responsableId = responsableId;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", responsableId=" + responsableId +
                '}';
    }
}


