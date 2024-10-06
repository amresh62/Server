package com.kamjritztex.aoms.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kamjritztex.aoms.models.AuditLog;
import com.kamjritztex.aoms.models.AuditLogFilter;
import com.kamjritztex.aoms.models.Employee;
import com.kamjritztex.aoms.repositories.AuditLogRepository;
import com.kamjritztex.aoms.repositories.EmployeeRepository;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<AuditLog> getFilteredAuditLogs(AuditLogFilter filter) {
        return auditLogRepository.findAll((Specification<AuditLog>) (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.getStartDate()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.getEndDate()));
            }
            if (filter.getAction() != null && !filter.getAction().isEmpty()) {
                predicates.add(cb.equal(root.get("action"), filter.getAction()));
            }
            if (filter.getPerformedById() != null) {
                predicates.add(cb.equal(root.get("performedBy").get("email"), filter.getPerformedById()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });
    }

    public AuditLog createAuditLog(String action, String details, String userId) {
        Employee employee = employeeRepository.findByEmail(userId).orElse(null);
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setDetails(details);
        auditLog.setPerformedBy(employee);
        auditLog.setTimestamp(LocalDateTime.now());
        return auditLogRepository.save(auditLog);
    }

}