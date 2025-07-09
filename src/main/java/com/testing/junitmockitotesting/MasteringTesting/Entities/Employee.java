package com.testing.junitmockitotesting.MasteringTesting.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id", nullable = false, updatable = false)
    private Long id;

    @NotBlank
    @Size(max = 100, message = "first name cannot exceed 100 characters")
    @Column(name = "first_name", nullable = false, length = 100)
    @Setter
    private  String firstName;

    @NotBlank
    @Size(max = 100, message = "last name cannot exceed 100 characters")
    @Column(name = "last_name", nullable = false, length = 100)
    @Setter
    private String lastName;

    @NotBlank
    @Email(message = "provide a valid email")
    @Column(name = "employee_email", nullable = false)
    @Setter
    private String email;

    @PastOrPresent(message = "Join date cannot be in the future")
    @Column(name = "join_date", nullable = false)
    @Setter
    private LocalDate joinDate;

    @CreationTimestamp
    @Column(name="created_date", updatable = false)
    private LocalDate createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    public static EmployeeBuilder create(String firstName, String lastName, String email, LocalDate joinDate){
        return new EmployeeBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .joinDate(joinDate)
                ;
    }

}
