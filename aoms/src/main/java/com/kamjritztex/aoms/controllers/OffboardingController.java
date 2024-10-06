package com.kamjritztex.aoms.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kamjritztex.aoms.models.AssetReturn;
import com.kamjritztex.aoms.models.AssetReturnStatus;
import com.kamjritztex.aoms.models.Employee;
import com.kamjritztex.aoms.models.ExitInterview;
import com.kamjritztex.aoms.models.OffboardingProcess;
import com.kamjritztex.aoms.models.OffboardingStatus;
import com.kamjritztex.aoms.models.Task;
import com.kamjritztex.aoms.models.TaskStatus;
import com.kamjritztex.aoms.services.OffboardingService;

@RestController
@RequestMapping("/api/offboarding")
public class OffboardingController {

    @Autowired
    private OffboardingService offboardingService;

    @GetMapping
    public ResponseEntity<List<OffboardingProcess>> getAllOffboardingProcesses() {
        return ResponseEntity.ok(offboardingService.getAllOffboardingProcesses());
    }

    @PostMapping("/initiate")
    public ResponseEntity<OffboardingProcess> initiateOffboarding(@RequestBody Employee employee,
            @RequestParam LocalDate lastWorkingDate) {
        return ResponseEntity.ok(offboardingService.initiateOffboarding(employee, lastWorkingDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffboardingProcess> getOffboardingProcess(@PathVariable Long id) {
        return ResponseEntity.ok(offboardingService.getOffboardingProcess(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOffboardingStatus(@PathVariable Long id, @RequestBody OffboardingStatus status) {
        return ResponseEntity.ok(offboardingService.updateOffboardingStatus(id, status));
    }

    @PostMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(offboardingService.getAllTasks());
    }

    @GetMapping("/tasks/pending")
    public ResponseEntity<List<Task>> getPendingTasks() {
        return ResponseEntity.ok(offboardingService.getPendingTasks());
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatus status) {

        return ResponseEntity.ok(offboardingService.updateTaskStatus(id, status));
    }

    @PostMapping("/task/{id}/approve")
    public ResponseEntity<Task> approveTask(@PathVariable Long id, @RequestBody Employee approver) {
        return ResponseEntity.ok(offboardingService.approveTask(id, approver));
    }

    @GetMapping("/asset-returns")
    public ResponseEntity<List<AssetReturn>> getAllAssetReturns() {
        return ResponseEntity.ok(offboardingService.getAllAssetReturns());
    }

    @PutMapping("/asset-return/{id}")
    public ResponseEntity<?> updateAssetReturnStatus(@PathVariable Long id, @RequestBody AssetReturnStatus status) {
        return ResponseEntity.ok(offboardingService.updateAssetReturnStatus(id, status));
    }

    @GetMapping("/exit-interviews")
    public ResponseEntity<List<ExitInterview>> getAllExitInterviews() {
        return ResponseEntity.ok(offboardingService.getAllExitInterviews());
    }

    @PostMapping("/exit-interview")
    public ResponseEntity<ExitInterview> scheduleExitInterview(@RequestBody ExitInterview exitInterview) {
        ExitInterview scheduledInterview = offboardingService.scheduleExitInterview(exitInterview);
        return ResponseEntity.ok(scheduledInterview);
    }

    @PutMapping("/exit-interview/{id}")
    public ResponseEntity<ExitInterview> updateExitInterview(@PathVariable Long id,
            @RequestBody ExitInterview exitInterview) {
        return ResponseEntity.ok(offboardingService.updateExitInterview(id, exitInterview));
    }
}
