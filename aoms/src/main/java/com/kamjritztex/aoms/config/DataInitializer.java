package com.kamjritztex.aoms.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kamjritztex.aoms.models.Department;
import com.kamjritztex.aoms.models.Employee;
import com.kamjritztex.aoms.models.OffboardingProcess;
import com.kamjritztex.aoms.models.Role;
import com.kamjritztex.aoms.models.TaskStatus;
import com.kamjritztex.aoms.repositories.EmployeeRepository;
import com.kamjritztex.aoms.services.OffboardingService;

@Configuration
public class DataInitializer {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OffboardingService offboardingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create employees
            Employee employee1 = employeeRepository.findByEmail("amresh.sinha@example.com").orElse(null);
            if(employee1 == null) {
                employee1 = new Employee();
                employee1.setFirstName("Amresh");
                employee1.setLastName("Sinha");
                employee1.setEmail("amresh.sinha@example.com");
                employee1.setPassword(passwordEncoder.encode("password"));
                employee1.setDepartment(Department.IT);
                employee1.setRole(Role.USER);
            }

            Employee employee2 = employeeRepository.findByEmail("abhishek.kumar@example.com").orElse(null);
            if(employee2 == null) {
                employee2 = new Employee();
                employee2.setFirstName("Abhishek");
                employee2.setLastName("Kumar");
                employee2.setEmail("abhishek.kumar@example.com");
                employee2.setPassword(passwordEncoder.encode("password"));
                employee2.setDepartment(Department.HR);
                employee2.setRole(Role.ADMIN);
                
            }
            Employee employee3 = employeeRepository.findByEmail("rishabh.raj@example.com").orElse(null);
            if(employee3 == null) {
                employee3 = new Employee();
                employee3.setFirstName("Rishabh");
                employee3.setLastName("Raj");
                employee3.setEmail("rishabh.raj@example.com");
                employee3.setPassword(passwordEncoder.encode("password"));
                employee3.setDepartment(Department.FINANCE);
                employee3.setRole(Role.SUPER_ADMIN);
                
            }

            employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));

            // Initiate offboarding 
            LocalDate lastWorkingDate = LocalDate.now().plusWeeks(2);
            OffboardingProcess process = offboardingService.initiateOffboarding(employee1, lastWorkingDate);
            process = offboardingService.initiateOffboarding(employee2, lastWorkingDate);
            process = offboardingService.initiateOffboarding(employee3, lastWorkingDate);

            // Update some task statuses
            offboardingService.updateTaskStatus(process.getTasks().get(0).getId(), TaskStatus.COMPLETED, employee1);
            offboardingService.updateTaskStatus(process.getTasks().get(1).getId(), TaskStatus.IN_PROGRESS, employee2);

            

            System.out.println("Database initialized with dummy data.");
        };
    }
}