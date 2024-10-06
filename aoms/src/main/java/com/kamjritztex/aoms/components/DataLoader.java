// package com.kamjritztex.aoms.components;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import com.kamjritztex.aoms.models.Department;
// import com.kamjritztex.aoms.models.Employee;
// import com.kamjritztex.aoms.models.Role;
// import com.kamjritztex.aoms.repositories.EmployeeRepository;

// @Component
// public class DataLoader implements CommandLineRunner {

//     @Autowired
//     private EmployeeRepository employeeRepository;

//     // @Autowired
//     // private TaskRepository taskRepository;

//     // @Autowired
//     // private AssetRepository assetRepository;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @Override
//     public void run(String... args) throws Exception {

//         Employee employee = new Employee();
//         for (int i = 1; i <= 10; i++) {
//             employee = new Employee();
//             employee.setFirstName("Employee" + i);
//             employee.setLastName("Test");
//             employee.setPassword(passwordEncoder.encode("1234"));
//             employee.setEmail("employee" + i + "@example.com");
//             employee.setRole(Role.USER);
//             employee.setDepartment(i % 2 == 0 ? Department.HR : Department.IT);
//             employeeRepository.save(employee);

//             // Task task = new Task();
//             // task.setName("Exit Interview");
//             // task.setStatus(TaskStatus.PENDING);
//             // task.setDescription(employee.getDepartment().getName());
//             // taskRepository.save(task);

//             // Asset asset = new Asset();
//             // asset.setName("Laptop");
//             // asset.setStatus(AssetStatus.PENDING);
//             // asset.setEmployee(employee);
//             // assetRepository.save(asset);
//         }
//         employee = new Employee();
//         employee.setFirstName("Employee" + 51);
//         employee.setLastName("Test");
//         employee.setPassword(passwordEncoder.encode("1234"));
//         employee.setEmail("employee" + 51 + "@example.com");
//         employee.setRole(Role.ADMIN);
//         employee.setDepartment(Department.ADMIN);
//         employeeRepository.save(employee);

//         employee = new Employee();
//         employee.setFirstName("Employee" + 52);
//         employee.setLastName("Test");
//         employee.setPassword(passwordEncoder.encode("1234"));
//         employee.setEmail("employee" + 52 + "@example.com");
//         employee.setRole(Role.SUPER_ADMIN);
//         employee.setDepartment(Department.ADMIN);
//         employeeRepository.save(employee);

//     }
// }
