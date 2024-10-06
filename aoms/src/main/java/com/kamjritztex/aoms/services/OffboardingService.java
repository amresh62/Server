package com.kamjritztex.aoms.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kamjritztex.aoms.models.AssetReturn;
import com.kamjritztex.aoms.models.AssetReturnStatus;
import com.kamjritztex.aoms.models.Department;
import com.kamjritztex.aoms.models.Employee;
import com.kamjritztex.aoms.models.ExitInterview;
import com.kamjritztex.aoms.models.FinalSettlement;
import com.kamjritztex.aoms.models.OffboardingProcess;
import com.kamjritztex.aoms.models.OffboardingStatus;
import com.kamjritztex.aoms.models.SettlementStatus;
import com.kamjritztex.aoms.models.Task;
import com.kamjritztex.aoms.models.TaskStatus;
import com.kamjritztex.aoms.repositories.AssetReturnRepository;
import com.kamjritztex.aoms.repositories.EmployeeRepository;
import com.kamjritztex.aoms.repositories.ExitInterviewRepository;
import com.kamjritztex.aoms.repositories.FinalSettlementRepository;
import com.kamjritztex.aoms.repositories.OffboardingProcessRepository;
import com.kamjritztex.aoms.repositories.TaskRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OffboardingService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OffboardingProcessRepository offboardingProcessRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ExitInterviewRepository exitInterviewRepository;

    @Autowired
    private AssetReturnRepository assetReturnRepository;

    @Autowired
    private FinalSettlementRepository finalSettlementRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public OffboardingProcess initiateOffboarding(Employee employee, LocalDate lastWorkingDate) {
        // Check if the employee exists
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        OffboardingProcess process = new OffboardingProcess();
        process.setEmployee(existingEmployee);
        process.setInitiationDate(LocalDate.now());
        process.setLastWorkingDate(lastWorkingDate);
        process.setStatus(OffboardingStatus.INITIATED);
        process.setTasks(new ArrayList<>()); // Initialize the tasks list

        process = offboardingProcessRepository.save(process);

        createOffboardingTasks(process);
        scheduleExitInterview(process);
        initializeAssetReturns(process);
        initializeFinalSettlement(process);

        notificationService.notifyHR(process);

        // Fetch the updated process with associated tasks
        return offboardingProcessRepository.findById(process.getId()).orElseThrow();
    }

    private void createOffboardingTasks(OffboardingProcess process) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(createTask("Complete exit paperwork", Department.HR, process));
        tasks.add(createTask("Revoke system access", Department.IT, process));
        tasks.add(createTask("Collect company assets", Department.ADMIN, process));
        tasks.add(createTask("Process final payroll", Department.FINANCE, process));

        taskRepository.saveAll(tasks);
        process.setTasks(tasks); // Set the tasks directly on the process
        offboardingProcessRepository.save(process); // Save the updated process
        auditLogService.createAuditLog("Offboarding Task Creation",
                "Offboarding Task initiated for " + process.getEmployee().getFirstName() + " "
                        + process.getEmployee().getLastName(),
                process.getEmployee().getEmail());
    }

    private Task createTask(String name, Department department, OffboardingProcess process) {
        Task task = new Task();
        task.setName(name);
        task.setStatus(TaskStatus.PENDING);
        task.setDepartment(department);
        task.setOffboardingProcess(process);
        task.setRequiresApproval(true);
        return task;
    }

    private void scheduleExitInterview(OffboardingProcess process) {
        ExitInterview exitInterview = new ExitInterview();
        exitInterview.setOffboardingProcess(process);
        exitInterview.setScheduledAt(process.getLastWorkingDate().atTime(10, 0));
        exitInterviewRepository.save(exitInterview);
        auditLogService.createAuditLog("Exit Interview Creation",
                "Exit Interview scheduled for " + process.getEmployee().getFirstName() + " "
                        + process.getEmployee().getLastName(),
                process.getEmployee().getEmail());
    }

    private void initializeAssetReturns(OffboardingProcess process) {
        // In a real scenario, you would fetch the employee's assigned assets
        List<AssetReturn> assetReturns = new ArrayList<>();
        assetReturns.add(createAssetReturn("Laptop", "LT001", process));
        assetReturns.add(createAssetReturn("Access Card", "AC001", process));
        assetReturnRepository.saveAll(assetReturns);
        auditLogService.createAuditLog("Asset Status Update",
                "Asset Status updated for " + process.getEmployee().getFirstName() + " "
                        + process.getEmployee().getLastName(),
                process.getEmployee().getEmail());
    }

    private AssetReturn createAssetReturn(String assetName, String assetId, OffboardingProcess process) {
        AssetReturn assetReturn = new AssetReturn();
        assetReturn.setAssetName(assetName);
        assetReturn.setAssetId(assetId);
        assetReturn.setStatus(AssetReturnStatus.PENDING);
        assetReturn.setOffboardingProcess(process);
        assetReturn.setReceivedBy(process.getEmployee());
        return assetReturn;
    }

    private void initializeFinalSettlement(OffboardingProcess process) {
        FinalSettlement finalSettlement = new FinalSettlement();
        finalSettlement.setOffboardingProcess(process);
        finalSettlement.setStatus(SettlementStatus.PENDING);
        finalSettlementRepository.save(finalSettlement);
        auditLogService.createAuditLog("Final Settlement Initializes",
                "Final Settlement Initialised for " + process.getEmployee().getFirstName() + " "
                        + process.getEmployee().getLastName(),
                process.getEmployee().getEmail());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getPendingTasks(TaskStatus status, Department department, Employee assignedTo) {
        List<Task> tasks = taskRepository.findAll();
        if (status != null) {
            tasks = tasks.stream().filter(task -> task.getStatus() == status).toList();
        }
        if (department != null) {
            tasks = tasks.stream().filter(task -> task.getDepartment() == department).toList();
        }
        if (assignedTo != null) {
            tasks = tasks.stream().filter(task -> task.getAssignedTo() == assignedTo).toList();
        }

        return tasks;
    }

    public List<Task> getPendingTasks() {
        List<Task> tasks = taskRepository.findByStatus(TaskStatus.PENDING);
        return tasks;
    }

    @Transactional
    public void updateTaskStatus(Long taskId, TaskStatus newStatus, Employee approver) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(newStatus);

        if (newStatus == TaskStatus.COMPLETED && task.isRequiresApproval()) {
            task.setApprovedBy(approver);
            task.setApprovedAt(LocalDate.now().atStartOfDay());
        }

        taskRepository.save(task);
        auditLogService.createAuditLog("Task Status Updated",
                "Task Status updated for " + task.getName(),
                approver.getEmail());
        updateOffboardingProcessStatus(task.getOffboardingProcess());

    }

    private void updateOffboardingProcessStatus(OffboardingProcess process) {
        List<Task> tasks = process.getTasks();
        boolean allCompleted = tasks.stream().allMatch(task -> task.getStatus() == TaskStatus.COMPLETED);

        if (allCompleted) {
            process.setStatus(OffboardingStatus.COMPLETED);
            notificationService.createNotification("Offboarding completed for " + process.getEmployee().getFirstName() + " " + process.getEmployee().getLastName());
        } else if (tasks.stream().anyMatch(task -> task.getStatus() == TaskStatus.IN_PROGRESS)) {
            process.setStatus(OffboardingStatus.IN_PROGRESS);
        } else {
            process.setStatus(OffboardingStatus.PENDING_APPROVALS);
        }

        offboardingProcessRepository.save(process);
        auditLogService.createAuditLog("Offboarding Process Status Updated",
                "Offboarding Process Status Updated " + process.getEmployee().getFirstName() + " " +
                        process.getEmployee().getLastName(),
                process.getEmployee().getEmail());
        
    }

    public List<OffboardingProcess> getAllOffboardingProcesses() {
        return offboardingProcessRepository.findAll();
    }

    public OffboardingProcess getOffboardingProcess(Long id) {
        return offboardingProcessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offboarding process not found"));
    }

    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task approveTask(Long id, Employee approver) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setApprovedBy(approver);
        task.setApprovedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<AssetReturn> getAllAssetReturns() {
        return assetReturnRepository.findAll();
    }

    public List<ExitInterview> getAllExitInterviews() {
        return exitInterviewRepository.findAll();
    }

    public ExitInterview scheduleExitInterview(ExitInterview exitInterview) {
        return exitInterviewRepository.save(exitInterview);
    }

    public ExitInterview updateExitInterview(Long id, ExitInterview exitInterview) {
        ExitInterview existingInterview = exitInterviewRepository.findById(exitInterview.getId())
                .orElseThrow(() -> new RuntimeException("Exit interview not found"));

        OffboardingProcess process = offboardingProcessRepository
                .findById(existingInterview.getOffboardingProcess().getId())
                .orElseThrow(() -> new RuntimeException("Offboarding process not found"));

        Employee employee = employeeRepository.findByEmail(process.getEmployee()
                .getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update fields of existingInterview with exitInterview
        existingInterview.setConductedAt(exitInterview.getConductedAt());
        existingInterview.setFeedback(exitInterview.getFeedback());
        existingInterview.setConductedBy(employee);
        ExitInterview updatedInterview = exitInterviewRepository.save(existingInterview);
        auditLogService.createAuditLog("Exit Interview Updated",
                "Exit Interview updated for " + employee.getFirstName() + " " + employee.getLastName(),
                employee.getEmail());
        updateOffboardingProcessStatus(process);

        return updatedInterview;
    }

    public Object updateAssetReturnStatus(Long id, AssetReturnStatus status) {
        AssetReturn assetReturn = assetReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset return not found"));
        assetReturn.setStatus(status);
        assetReturn.setReturnedAt(LocalDateTime.now());
        return assetReturnRepository.save(assetReturn);
    }

    public Object updateOffboardingStatus(Long id, OffboardingStatus status) {
        OffboardingProcess offboardingProcess = offboardingProcessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offboarding process not found"));
        offboardingProcess.setStatus(status);
        return offboardingProcessRepository.save(offboardingProcess);
    }
}