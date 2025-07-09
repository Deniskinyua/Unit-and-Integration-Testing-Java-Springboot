package com.testing.junitmockitotesting.MasteringTesting.Repository;


import com.testing.junitmockitotesting.MasteringTesting.Entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer
            = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUpDatabase(){
        testEntityManager.getEntityManager()
                .getMetamodel()
                .entity(Employee.class);
    }

    @Test
    void connectionEstablished(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    @Transactional
    void findEmployeeByEmail_ShouldReturnEmployee_WhenExists(){
        String email = "jonesKariuki@gmail.com";
       Employee employee = Employee.create("Jones",
                "Kariuki",
               email,
                LocalDate.now())
               .build();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
        testEntityManager.flush();

        Optional<Employee> returnedEmployee =
                employeeRepository.findEmployeeByEmail(savedEmployee.getEmail());

        assertThat(returnedEmployee)
                .isPresent()
                .get()
                .extracting(Employee::getEmail)
                .isEqualTo(email);
    }

    @Test
    void save_ShouldPersistEmployeeWithAllRequiredFields(){
        // Given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .joinDate(LocalDate.now())
                .build();

        // When
        Employee saved = employeeRepository.save(employee);
        testEntityManager.flush();
        testEntityManager.clear();

        // Then
        assertThat(saved.getId()).isNotNull();
        Employee found = testEntityManager.find(Employee.class, saved.getId());
        assertThat(found).usingRecursiveComparison().isEqualTo(employee);
    }

}
