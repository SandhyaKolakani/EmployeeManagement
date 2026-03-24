package com.employee.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.employee.rest.model.Employee;
import com.employee.rest.service.EmployeeService;

@SpringBootTest
public class EmployeeServiceTest {

	@Autowired
	public EmployeeService employeeService;
	
	@Test
	void testSaveEmployeeWithSalaryCalculation() {
		Employee employee = new Employee( null, "Ravi", "IT", "Male", 10000.0, "ravi@gmail.com", 9876543210L );
		
		Employee savedEmployee = employeeService.saveEmployeeData(employee);
		
		assertNotNull(savedEmployee.getId());
		assertEquals(13100.0, savedEmployee.getSalary());
	}
	@Test
	void testSaveEmployeeWithNegativeSalary() {

	    // Input with negative salary
		
	    Employee employee = new Employee( null, "John", "HR", "Male", -5000.0, "john@gmail.com", 9876543211L );

	    // Expect RuntimeException
	    assertThrows(RuntimeException.class, () -> {
	        employeeService.saveEmployeeData(employee);
	    });
	}
	
	
}
