package com.example.project_service.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class AddEmployeeRequest {
    @NotNull(message = "Employee IDs are required")
    private Set<Long> employeeIds;

    public Set<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(Set<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
} 