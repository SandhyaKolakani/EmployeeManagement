package com.employee.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.employee.rest.model.Employee;
import com.employee.rest.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    
    
    /*------------------------------
     		Save Employee
     -------------------------------*/
    
    @Test
    void testSaveEmployee() throws Exception {

        Employee employee = new Employee(
                1L,
                "Ravi",
                "IT",
                "Male",
                13100.0,
                "ravi@gmail.com",
                9876543210L
        );

        when(employeeService.saveEmployeeData(ArgumentMatchers.any(Employee.class)))
                .thenReturn(employee);

        String json = """
                {
                    "name": "Ravi",
                    "department": "IT",
                    "gender": "Male",
                    "salary": 10000,
                    "email": "ravi@gmail.com",
                    "mobile": 9876543210
                }
                """;

        mockMvc.perform(post("/saveEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ravi"))
                .andExpect(jsonPath("$.salary").value(13100.0));
    }
    
    
    
    /*------------------------------
		  Get Employee By ID
	-------------------------------*/
    
    @Test
    void testGetEmployeeById() throws Exception {

        Employee employee = new Employee(
                1L,
                "Ravi",
                "IT",
                "Male",
                13100.0,
                "ravi@gmail.com",
                9876543210L
        );

        when(employeeService.getEmployeeById(1L))
                .thenReturn(employee);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/getEmployees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ravi"));
    }
    
   
    
    /*------------------------------
	  		Get All Employees
	-------------------------------*/
    
    @Test
    void testGetAllEmployees() throws Exception {

        java.util.List<Employee> employees = java.util.List.of(
                new Employee(1L, "Ravi", "IT", "Male", 13100.0, "ravi@gmail.com", 9876543210L)
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/getAllEmployees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ravi"));
    }

    /*------------------------------
	  	 Delete Employee By ID
	-------------------------------*/

    @Test
    void testDeleteEmployee() throws Exception {

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/deleteEmployee/1"))
                .andExpect(status().isNoContent());
    }
    
    
    /*------------------------------
 	   	Update Employee By ID
	-------------------------------*/
    
    @Test
    void testUpdateEmployee() throws Exception {

        Employee updated = new Employee(
                1L,
                "Ravi Kumar",
                "IT",
                "Male",
                20000.0,
                "ravi@gmail.com",
                9876543210L
        );

        when(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(updated);

        String json = """
                {
                    "id":1,
                    "name":"Ravi Kumar",
                    "department":"IT",
                    "gender":"Male",
                    "salary":20000,
                    "email":"ravi@gmail.com",
                    "mobile":9876543210
                }
                """;

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/updateEmployee")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ravi Kumar"));
    }
    
    
}