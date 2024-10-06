package com.kamjritztex.aoms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamjritztex.aoms.models.AuditLog;
import com.kamjritztex.aoms.models.AuditLogFilter;
import com.kamjritztex.aoms.services.AuditLogService;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<List<AuditLog>> getFilteredAuditLogs(@RequestBody AuditLogFilter filter) {
        return ResponseEntity.ok(auditLogService.getFilteredAuditLogs(filter));
    }
}

