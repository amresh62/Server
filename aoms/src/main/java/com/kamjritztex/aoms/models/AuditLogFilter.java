package com.kamjritztex.aoms.models;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AuditLogFilter {
    private LocalDate startDate;
    private LocalDate endDate;
    private String action;
    private Long performedById;
}