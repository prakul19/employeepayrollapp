package com.bridgelabz.employeepayrollapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    private String gender;

    private Double salary;

    private LocalDate startDate;

    // Custom constructor without ID
    public Employee(String name, String department, String gender, Double salary, LocalDate startDate) {
        this.name = name;
        this.department = department;
        this.gender = gender;
        this.salary = salary;
        this.startDate = startDate;
    }
}
