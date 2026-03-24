package com.employee.rest.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import com.employee.rest.EmployeeRecordsRestApplication;
import com.employee.rest.exception.EmployeeNotFoundException;

import com.employee.rest.model.Employee;
import com.employee.rest.repository.EmployeeRepository;

@Service
	public class EmployeeService {

		@Autowired
		private EmployeeRepository employeeRepository;
	
		/*-------------------------------------------
		 		Saving Single Employee Data
		 --------------------------------------------*/
		
		public Employee saveEmployeeData(Employee employee) {
			
			if(employee.getSalary() <=0 ) {
		 		throw new RuntimeException("Salary must not be lessthan 0");
			}
		
			double basicSalary = employee.getSalary();

			// Calculate HRA, DA, PF
			double hra = basicSalary * 0.30;  
			double da  = basicSalary * 0.05;  
			double pf  = basicSalary * 0.04;  

			// Calculate final salary
			double finalSalary = basicSalary + hra + da - pf;

			// Update employee salary
			employee.setSalary(finalSalary);

			return employeeRepository.save(employee);
		}
		
		
		/*-------------------------------------------
 				Saving Multiple Employees Data
 		--------------------------------------------*/

		public List<Employee> saveAllEmployeeData(List<Employee> employees) {
			 
			    for (Employee employee : employees) {
			        if (employee.getSalary() <= 0) {
			            throw new RuntimeException("salary must be greater than 0");
			        }
			        double basicSalary = employee.getSalary();

					// Calculate HRA, DA, PF
					double hra = basicSalary * 0.30;  
					double da  = basicSalary * 0.05;  
					double pf  = basicSalary * 0.04;  

					// Calculate final salary
					double finalSalary = basicSalary + hra + da - pf;

					// Update employee salary
					employee.setSalary(finalSalary);
			       
			    }
			    return employeeRepository.saveAll(employees);
			}
		
		
		/*-------------------------------------------
 				Retrieving Employee Based on ID
 		 --------------------------------------------*/

		public Employee getEmployeeById(Long id) {
			Optional<Employee> optionalEmployee = employeeRepository.findById(id);
			if(optionalEmployee.isPresent()) {
				Employee employee = optionalEmployee.get();
				return employee;
			}
			else {
				throw new EmployeeNotFoundException("Employee is not present with id "+id);
			}
			
		}

		
		/*-------------------------------------------
				Retrieving All Employee Records
	 	--------------------------------------------*/

		public List<Employee> getAllEmployees() {
			 return employeeRepository.findAll();
		}
	
	
		/*-------------------------------------------
				Deleting Employee Based on ID
	 	--------------------------------------------*/

		public void deleteEmployeeById(Long id) {
			if(employeeRepository.existsById(id)) {
				employeeRepository.deleteById(id);
			}
			else {
				throw new EmployeeNotFoundException("Employee Not Found With This ID "+id);
			}
		}

		
		/*-------------------------------------------
				Deleting All Employee Records 
		--------------------------------------------*/

		public void deleteAllEmployees() {
			if(employeeRepository.count()>0) {
				employeeRepository.deleteAll();
			}
			else {
				throw new EmployeeNotFoundException("All The Employee Records Were Deleted");
			}
			
		}


		/*----------------------------------------------------
			 Updating Entire Employee Details Based On ID  
		------------------------------------------------------*/
		
		public Employee updateEmployee(Long id, Employee employee) {

		    Optional<Employee> optionalEmployee = employeeRepository.findById(id);

		    if (optionalEmployee.isPresent()) {

		        Employee employeeUpdate = optionalEmployee.get();

		        employeeUpdate.setName(employee.getName());
		        employeeUpdate.setDepartment(employee.getDepartment());
		        employeeUpdate.setGender(employee.getGender());
		        employeeUpdate.setSalary(employee.getSalary());
		        employeeUpdate.setEmail(employee.getEmail());
		        employeeUpdate.setMobile(employee.getMobile());
		        

		        return employeeRepository.save(employeeUpdate);

		    } 
		    else {
		        throw new EmployeeNotFoundException("Employee is not present with id " + id);
		    }
		}


		/*---------------------------------------------------------
			 Updating Specific Details Of Employee Based On ID  
		-----------------------------------------------------------*/
		
		public Employee partialUpdateEmployeeData(Long id, Map<String, Object> updateDetails) {
			Optional<Employee> optionalEmployee = employeeRepository.findById(id);
			if(optionalEmployee.isPresent()) {
				Employee employee = optionalEmployee.get();
				
				updateDetails.forEach((key,value) -> {
					switch (key) {
					
					case "name" : employee.setName((String)value);
									break;
					case "salary" : employee.setSalary((Double)value);
									break;
					case "email" : employee.setEmail((String)value);
									break;
					case "mobile" : employee.setMobile((Long)value);
									break;
					}
					
					
				});
					return employeeRepository.save(employee);
				}
				else {
					throw new EmployeeNotFoundException("Employee Not Found With Id "+id);
							
				}
			}
		
		
		/*-------------------------------------------
			Retrieving Employee Based On Email
		--------------------------------------------*/
		
		    public Employee getEmployeeByEmail(String email) {

		        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);

		        if(optionalEmployee.isPresent()) {
					Employee employee = optionalEmployee.get();
					return employee;
				}
				else {
					throw new EmployeeNotFoundException("Employee is not present with Email "+email);
				}
		}

		
	    /*------------------------------------------------------
				Filtering Minimum Salary Of Employees 
		--------------------------------------------------------*/

		public List<Employee> filterByMinSalary(double minSalary) {
			List<Employee> employees = employeeRepository.findBySalaryGreaterThanEqual(minSalary);
			
			return employees;
		}


		/*------------------------------------------------------
				Filtering Maximum Salary Of Employees 
		--------------------------------------------------------*/
		
		public List<Employee> filterByMaxSalary(double maxSalary) {
			List<Employee> employees = employeeRepository.findBySalaryLessThanEqual(maxSalary);
			
			return employees;
		}
		
		
		/*-------------------------------------------------
		 	Retrieving Employees Based On Salary Range
		---------------------------------------------------*/

		public List<Employee> filterByRange(double minSalary, double maxSalary) {
			List<Employee> employees = employeeRepository.findBySalaryBetween(minSalary,maxSalary);
			
			return employees;
		}

		
		/*----------------------------------------------------
		 	Retrieving Employees Based On Salary OR Gender
		------------------------------------------------------*/

		public List<Employee> filterBySalaryOrGender(double salary, String gender) {
			List<Employee> employees = employeeRepository.findBySalaryGreaterThanOrGender(salary,gender);
			
			return employees;
		}

		
		/*----------------------------------------------------------------
	 		Retrieving Employees Based On Gender AND Salary GreaterThan
		------------------------------------------------------------------*/

		public List<Employee> getByGenderAndSalaryGreater(String gender, double salary) {
			List<Employee> employees = employeeRepository.findByGenderAndSalaryGreaterThan(gender, salary);
			
			return employees;
		}
		
		
		/*----------------------------------------------------------
 			Retrieving Employees Based On Gender OR Salary Range
		------------------------------------------------------------*/

		public List<Employee> getByGenderOrSalaryBetween(String gender, double minSalary, double maxSalary) {
			List<Employee> employees = employeeRepository.findByGenderOrSalaryBetween(gender, minSalary, maxSalary);
		
			return employees;
		}

		
		/*-----------------------------------------------------------
 			Sorting Employees Based On Salary In Ascending Order
		-------------------------------------------------------------*/

		public List<Employee> sortBySalaryAsc() {
			List<Employee> employees = employeeRepository.findAllByOrderBySalaryAsc();
			
			return employees;
		}

		
		/*-----------------------------------------------------------
			Sorting Employees Based On Salary In Descending Order
		-------------------------------------------------------------*/

		public List<Employee> sortBySalaryDesc() {
			List<Employee> employees = employeeRepository.findAllByOrderBySalaryDesc();
			
			return employees;
		}

		
		/*-------------------------------------------------
			Retrieving Employee Names Using IgnoreCase
		--------------------------------------------------*/

		public List<Employee> findByNameIgnoreCase(String name) {
			List<Employee> employees = employeeRepository.findByNameIgnoreCase(name);
			return employees;
		}

		
		/*---------------------------------------------
		 	 Retrieving Employee Names Containing
		-----------------------------------------------*/

		public List<Employee> searchByName(String name) {
			List<Employee> employees = employeeRepository.findByNameContaining(name);
			return employees;
		}
		
		/*---------------------------------------------
	 		 Retrieving Employee Based On Conditions
		-----------------------------------------------*/

		public List<Employee> filterEmployee(String department, String gender, double minSalary, double maxSalary) {
			List<Employee> employees = employeeRepository.searchEmployees(department, gender, minSalary, maxSalary);
			return employees;
		}

		
		/*---------------------------------------------
 			Deleting Employee Based On Salary Range
		-----------------------------------------------*/

		public void deleteEmployeeBySalaryRange(double minSalary, double maxSalary) {
			 employeeRepository.deleteEmployeeBySalaryRange(minSalary,maxSalary);
			
		}

		
		/*---------------------------------------------
				Deleting Employee Based On Email
		-----------------------------------------------*/
		
		 public Employee deleteEmployeeByEmail(String email) {

		        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);

		        if(optionalEmployee.isPresent()) {
					Employee employee = optionalEmployee.get();
					employeeRepository.deleteByEmail(email);
					return employee;
				}
				else {
					throw new EmployeeNotFoundException("Employee is not present with Email "+email);
				}
		}

//
//		 public Page<Employee> getEmployeesWithPagination(int page, int size) {
//
//			    Pageable pageable = PageRequest.of(page, size);
//			    return employeeRepository.findAll(pageable);
//			}


		 public List<Employee> getAllEmployees1() {
		        return employeeRepository.findAll();
		    }

		
		
		

		


		
		
		
		
	}


	
	
		
	


