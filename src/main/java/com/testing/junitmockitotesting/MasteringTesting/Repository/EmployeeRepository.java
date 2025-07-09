package com.testing.junitmockitotesting.MasteringTesting.Repository;

import com.testing.junitmockitotesting.MasteringTesting.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByEmail(String email);
}
