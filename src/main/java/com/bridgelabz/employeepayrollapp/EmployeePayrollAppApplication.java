package com.bridgelabz.employeepayrollapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.bridgelabz.employeepayrollapp.model")
public class EmployeePayrollAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmployeePayrollAppApplication.class, args);
	}
}
