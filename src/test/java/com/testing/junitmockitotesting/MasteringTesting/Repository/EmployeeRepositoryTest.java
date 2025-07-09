package com.testing.junitmockitotesting.MasteringTesting.Repository;


import com.testing.junitmockitotesting.MasteringTesting.Entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
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

    /**
     * Helper Methods:
     *  Handles persistence logic
     *  You need persisted object for repository tests
     * */
    private Employee persistTestEmployee(String firstName, String lastName, String email) {
        return persistTestEmployee(
                Employee.create(firstName, lastName, email, LocalDate.now()).build()
        );
    }

    private Employee persistTestEmployee(Employee employee) {
        testEntityManager.persist(employee);
        testEntityManager.flush();
        testEntityManager.clear();
        return employee;
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

    @Test
    @Transactional
    void update_shouldModifyExistingEmployee() {
        // Given
        Employee employee = persistTestEmployee("Mike", "Johnson", "mike@example.com");
        String newLastName = "Williams";

        // When
        employee.setLastName(newLastName);
        employeeRepository.saveAndFlush(employee);
        testEntityManager.clear();

        // Then
        Employee updated = testEntityManager.find(Employee.class, employee.getId());
        assertThat(updated.getLastName()).isEqualTo(newLastName);
    }

    @Test
    @Transactional
    void delete_shouldRemoveEmployeeFromDatabase() {
        // Given
        Employee employee = persistTestEmployee("Sarah", "Connor", "sarah@gmail.com");

        // When
        employeeRepository.delete(employee);
        testEntityManager.flush();

        // Then
        assertThat(testEntityManager.find(Employee.class, employee.getId())).isNull();
    }

    @Test
    @Transactional
    void findAll_shouldReturnAllEmployees() {
        // Given
        persistTestEmployee("Alice", "Brown", "alice@gmail.com");
        persistTestEmployee("Bob", "Green", "bob@gmail.com");

        // When
        List<Employee> allEmployees = employeeRepository.findAll();

        // Then
        assertThat(allEmployees)
                .hasSize(2)
                .extracting(Employee::getEmail)
                .containsExactlyInAnyOrder("alice@gmail.com", "bob@gmail.com");
    }

    @Test
    @Transactional
    void findEmployeeByEmail_shouldReturnEmpty_whenNotFound() {
        // When
        Optional<Employee> found = employeeRepository.findEmployeeByEmail("nonexistent@gmail.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @Transactional
    void save_shouldThrowException_whenEmailExists() {
        // Given
        String email = "duplicate@gmail.com";
        persistTestEmployee("First", "User", email);

        // When/Then
        assertThatThrownBy(() ->
                employeeRepository.save(Employee.builder()
                        .firstName("Second")
                        .lastName("User")
                        .email(email)
                        .joinDate(LocalDate.now())
                        .build()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    void save_shouldSetCreationTimestamp() {
        // When
        Employee saved = employeeRepository.save(Employee.builder()
                .firstName("Tim")
                .lastName("Cook")
                .email("tim@gmail.com")
                .joinDate(LocalDate.now())
                .build());
        testEntityManager.flush();

        // Then
        assertThat(saved.getCreatedDate()).isNotNull();
        assertThat(saved.getCreatedDate()).isBeforeOrEqualTo(LocalDate.now());
    }

    @Test
    @Transactional
    void update_shouldModifyLastModifiedDate() {
        // Given
        Employee employee = persistTestEmployee("Original", "Name", "original@gmail.com");
        LocalDate initialModifiedDate = employee.getLastModifiedDate();

        // When
        employee.setFirstName("Updated");
        employeeRepository.saveAndFlush(employee);
        testEntityManager.clear();

        // Then
        Employee updated = testEntityManager.find(Employee.class, employee.getId());
        assertThat(updated.getLastModifiedDate())
                .isAfter(initialModifiedDate);
    }

}
