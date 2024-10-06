package com.kamjritztex.aoms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kamjritztex.aoms.models.Department;
import com.kamjritztex.aoms.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    List<Employee> findByDepartment(Department department);
    Optional<Employee> findByEmail(String email);

}
