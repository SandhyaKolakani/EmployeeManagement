package com.employee.rest.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.employee.rest.model.Employee;
import com.employee.rest.service.EmployeeService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



	@RestController

	public class EmployeeController {
		
		@Autowired
		private EmployeeService employeeService;

		@CrossOrigin(origins = "*")
		@GetMapping("/greet")
		
		public String wish() {
			return "PROJECT ON EMPLOYEE RECORDS SYSTEM USING SPRING BOOT....!";
		}
		
		
		/*-------------------------------------------
 				Saving Single Employee Data
 		--------------------------------------------*/

		@PostMapping("/saveEmployee")
		public ResponseEntity<Employee> savedEmployeeData(@RequestBody @Valid Employee employee){
			Employee savedEmployee = employeeService.saveEmployeeData(employee);
			
			return ResponseEntity.status(HttpStatus.CREATED)
								 .header("Info","Employee Data is Saved...")
								 .body(savedEmployee);
		}
		
		/*-------------------------------------------
 				Saving Multiple Employees Data
 		--------------------------------------------*/
		
		@PostMapping("/saveAllEmployees")
		public ResponseEntity<List<Employee>> savedAllEmployeeData(@RequestBody List<Employee> employees){
			List<Employee> saveAllEmployees = employeeService.saveAllEmployeeData(employees);
			
			return ResponseEntity.status(HttpStatus.CREATED)
								 .header("Info","Employees Data is Saved...")
								 .body(saveAllEmployees);
		}
		
		
		/*-------------------------------------------
 				Retrieving Employee Based on ID
 		--------------------------------------------*/

		@GetMapping("/getEmployees/{id}")
		public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
			Employee employee = employeeService.getEmployeeById(id);
			
			return ResponseEntity.status(HttpStatus.OK)
								 .header("Info","Employee Data is Retrieved...")
								 .body(employee);
		
		}
		
		
		/*-------------------------------------------
				Retrieving All Employee Records
		--------------------------------------------*/
		
		@CrossOrigin(origins = "*")
		@GetMapping("/getAllEmployees")
		public ResponseEntity<List<Employee>> getAllEmployees() {

		    List<Employee> employees = employeeService.getAllEmployees();

		    return ResponseEntity.status(HttpStatus.OK)
		            .header("Info", "All Employee Data Retrieved...")
		            .body(employees);
		}

		
		/*-------------------------------------------
				Deleting Employee Based on ID
		--------------------------------------------*/
		
		@DeleteMapping("/deleteEmployee/{id}") 
		public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id){
			
			employeeService.deleteEmployeeById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			
		}
		
		
		/*-------------------------------------------
				Deleting All Employee Records 
		--------------------------------------------*/
		
		@DeleteMapping("/deleteAllEmployees")
		public ResponseEntity<Void> deleteAllEmployees(){
			employeeService.deleteAllEmployees();
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		
		/*----------------------------------------------------
			  Updating Entire Employee Details Based On ID   
		------------------------------------------------------*/
		
		@PutMapping("/updateEmployee/{id}")
		public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,@RequestBody Employee employee) {

		    Employee updatedEmployee = employeeService.updateEmployee(id, employee);

		    return ResponseEntity.status(HttpStatus.OK)
		            .header("Info", "Employee Updated Successfully...")
		            .body(updatedEmployee);
		}
		
		
		
		/*---------------------------------------------------------
		  	  Updating Specific Details Of Employee Based On ID  
		-----------------------------------------------------------*/
	
		@PatchMapping("/partialUpdate/{id}")
		public ResponseEntity<Employee> partialUpdateEmployeeData(@PathVariable Long id,
											@RequestBody Map<String, Object> updateDetails){
			
			Employee updateEmployee = employeeService.partialUpdateEmployeeData(id,updateDetails);
			return ResponseEntity.status(HttpStatus.OK)
								 .header("Info", "Employee Details were Updated")
								 .body(updateEmployee);
		}
		
		
		/*-------------------------------------------
			  Retrieving Names By Using CORS 
		--------------------------------------------*/
		
		@CrossOrigin(origins = "*")
		@GetMapping("/getNames")
		public ResponseEntity<List<String>> getNames(){
			return ResponseEntity.status(HttpStatus.OK)
								 .header("Info", "Data Retrieved...")
								 .body(List.of("Alex","John","Michale","Simon"));
		}
		
		
		/*------------------------------------------------
		  	 			Random Quote API
		-------------------------------------------------*/
		
		@GetMapping("/randomQuotes")
		public ResponseEntity<String> getQuotation(){
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForEntity("https://dummyjson.com/quotes/random", String.class);
		}
		
		
		
		/*------------------------------------------------
	  	 	Communicating One REST With Another REST
		-------------------------------------------------*/
		
		@GetMapping("/postalPincode/{pincode}")
		public ResponseEntity<String> getPincodeDetails(@PathVariable int pincode){
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("user-Agent","Mozilla/5.0");
			headers.set("Accept","application/json");
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			return restTemplate.exchange("https://api.postalpincode.in/pincode/"+pincode,
								HttpMethod.GET,
								entity,String.class);
			
		}
		
		
		
		/*-------------------------------------------
			Saving Employee Data Using HATEOS
		--------------------------------------------*/	

		@PostMapping("/saveEmployeeWithHateoas")
		public ResponseEntity<EntityModel<Employee>> saveEmployee(@RequestBody Employee employee){
			Employee saveEmployee = employeeService.saveEmployeeData(employee);
			EntityModel<Employee> entityModel = EntityModel.of(saveEmployee);
			
			// Creating The GET Link
			
			Link getLink = linkTo(
					methodOn(EmployeeController.class)
					.getEmployeeById(saveEmployee.getId())
					).withRel("get");
			
			
			// Creating The DELETE Link
			
			Link deleteLink = linkTo(
					methodOn(EmployeeController.class)
					.deleteEmployeeById(saveEmployee.getId())
					).withRel("delete");
			
			
			// Creating The PUT Link
			
			Link putLink = linkTo(
					methodOn(EmployeeController.class)
					.updateEmployee(saveEmployee.getId(),saveEmployee)
					).withRel("put");
			
			
			// Creating The PATCH Link
			
			Link patchLink = linkTo(
					methodOn(EmployeeController.class)
					.partialUpdateEmployeeData(saveEmployee.getId(),new HashMap<>())
					).withRel("patch");
			
			entityModel.add(getLink);
			entityModel.add(deleteLink);
			entityModel.add(putLink);
			entityModel.add(patchLink);
			
			  return ResponseEntity.status(HttpStatus.CREATED)
			            .header("Info", "Employee Saved with HATEOAS links")
			            .body(entityModel);
		
		}
//		
//		@GetMapping("/weatherReport")
//		public ResponseEntity<String> getWeatherReport(){
//			RestTemplate restTemplate = new RestTemplate();
//			return restTemplate.getForEntity(null, null)
//		}
//		
//			
		/*-------------------------------------------
			Retrieving Employee Based On Email
		--------------------------------------------*/
		
		@GetMapping("/employees/{email}")
		public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {

		    Employee employee = employeeService.getEmployeeByEmail(email);

		    return ResponseEntity.status(HttpStatus.OK)
		            .header("Info", "Employee Retrieved by Email")
		            .body(employee);
		}


		/*---------------------------------------------------------
				Filtering Minimum Salary Of Employees 
		-----------------------------------------------------------*/
		
		@GetMapping("/filterByMinSalary")
		public ResponseEntity<List<Employee>> filterByMinSalary(@RequestParam double minSalary){
			List<Employee> filterByMinSalary = employeeService.filterByMinSalary(minSalary);
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Updated")
					 .body(filterByMinSalary);
		}
		
		
		/*---------------------------------------------------------
				Filtering Maximum Salary Of Employees 
		-----------------------------------------------------------*/

		@GetMapping("/filterByMaxSalary")
		public ResponseEntity<List<Employee>> filterByMaxSalary(@RequestParam double maxSalary){
			List<Employee> filterByMaxSalary = employeeService.filterByMaxSalary(maxSalary);
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Updated")
					 .body(filterByMaxSalary);
		}
		
		
		/*-------------------------------------------------
			 Retrieving Employees Based On Salary Range
		---------------------------------------------------*/
		
		@GetMapping("/salaryByRange")

		 public ResponseEntity<List<Employee>> filterByRange(@RequestParam double minSalary,
		            										 @RequestParam double maxSalary) {
			 List<Employee> filterSalaryByRange = employeeService.filterByRange(minSalary,maxSalary);
				return ResponseEntity.status(HttpStatus.OK)
						 .header("Info", "Employee Details were Retrieved")
						 .body(filterSalaryByRange);
		}
		
		
		/*----------------------------------------------------
			 Retrieving Employees Based On Salary OR Gender
		------------------------------------------------------*/
			
		@GetMapping("/salaryOrGender")
	    public ResponseEntity<List<Employee>> filterBySalaryOrGender(@RequestParam double salary,
	    															 @RequestParam String gender) {
			List<Employee> filterBySalaryOrGender = employeeService.filterBySalaryOrGender(salary,gender);
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Retrieved")
					 .body(filterBySalaryOrGender);
		}
		
		
		/*----------------------------------------------------------------
		 	Retrieving Employees Based On Gender AND Salary GreaterThan
		------------------------------------------------------------------*/
		
		@GetMapping("/genderAndSalaryGreater")
		public ResponseEntity<List<Employee>> getByGenderAndSalaryGreater(@RequestParam String gender,
																		  @RequestParam double salary) {

			List<Employee> getByGenderAndSalary = employeeService.getByGenderAndSalaryGreater(gender,salary);
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Retrieved")
					 .body(getByGenderAndSalary);
		}
		
		
		/*----------------------------------------------------------
	 		Retrieving Employees Based On Gender OR Salary Range
		------------------------------------------------------------*/
		
		@GetMapping("/genderOrSalaryBetween")
		public ResponseEntity<List<Employee>> getByGenderOrSalaryBetween(@RequestParam String gender,
																         @RequestParam double minSalary,
																         @RequestParam double maxSalary) {
			List<Employee> filterByGenderOrSalaryBetween = employeeService.getByGenderOrSalaryBetween(gender,minSalary,maxSalary);
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Retrieved")
					 .body(filterByGenderOrSalaryBetween);
		}
		
		
		/*-----------------------------------------------------------
	 		Sorting Employees Based On Salary In Ascending Order
		-------------------------------------------------------------*/
				
		@GetMapping("/sortBySalaryAsc")
		public ResponseEntity<List<Employee>> sortBySalaryAsc() {
		   
			List<Employee> salaryAscOrder = employeeService.sortBySalaryAsc();
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Retrieved")
					 .body(salaryAscOrder);
		}

		
		/*-----------------------------------------------------------
 			Sorting Employees Based On Salary In Descending Order
		-------------------------------------------------------------*/
		
		@GetMapping("/sortBySalaryDesc")
		public ResponseEntity<List<Employee>> sortBySalaryDesc() {
		   
			List<Employee> salaryDescOrder = employeeService.sortBySalaryDesc();
			return ResponseEntity.status(HttpStatus.OK)
					 .header("Info", "Employee Details were Retrieved")
					 .body(salaryDescOrder);
		}

		
		/*-------------------------------------------------
 			Retrieving Employee Names Using IgnoreCase
		--------------------------------------------------*/
		
		@GetMapping("/filterName/{name}")
		public ResponseEntity<List<Employee>> findByNameIgnoreCase(@PathVariable String name) {

		    List<Employee> filterByName = employeeService.findByNameIgnoreCase(name);
		    return ResponseEntity.status(HttpStatus.OK)
		            .header("Info", "Employees Retrieved by name")
		            .body(filterByName);
		}


		/*---------------------------------------------
			 Retrieving Employee Names Containing
		-----------------------------------------------*/
		
		@GetMapping("/namesContaining")
		public ResponseEntity<List<Employee>> searchByName(@RequestParam String name) {

			List<Employee> findByName = employeeService.searchByName(name);
			 return ResponseEntity.status(HttpStatus.OK)
			            .header("Info", "Employees Retrieved by name")
			            .body(findByName);
			}

		
		/*---------------------------------------------
		 	Retrieving Employee Based On Conditions
		-----------------------------------------------*/
		
		@GetMapping("filterEmployee")
		public ResponseEntity<List<Employee>> filterEmployee(@RequestParam String department,
															 @RequestParam String gender,
															 @RequestParam double minSalary,
															 @RequestParam double maxSalary){ 
		
			List<Employee> filterEmployee = employeeService.filterEmployee(department,gender,minSalary,maxSalary);
			return ResponseEntity.status(HttpStatus.OK)
		            .header("Info", "Employee Records were Retrieved")
		            .body(filterEmployee);
		}
		
		
		/*---------------------------------------------
	 		Deleting Employee Based On Salary Range
		-----------------------------------------------*/
		
		@DeleteMapping("/deleteEmployeeByRange")
		public ResponseEntity<Void> deleteEmployeeBySalaryRange(@RequestParam double minSalary,
																@RequestParam double maxSalary){
			employeeService.deleteEmployeeBySalaryRange(minSalary,maxSalary);
			return ResponseEntity.noContent().build();
		}
		
				
		/*---------------------------------------------
	 			Deleting Employee Based On Email
		-----------------------------------------------*/
		
		@DeleteMapping("/deleteByEmail/{email}")
		public ResponseEntity<Employee> deleteEmployeeByEmail(@PathVariable String email) {

		    employeeService.deleteEmployeeByEmail(email);

		    return ResponseEntity.noContent().build();
		}
		
		
		/*---------------------------------------------
					Pagination API
		-----------------------------------------------*/
	  
//		@CrossOrigin(origins = "*")
//		@GetMapping("/EmployeeList")
//		
//		public Page<Employee> getEmployees( @RequestParam int page,
//		        							@RequestParam int size) {
//
//		    return employeeService.getEmployeesWithPagination(page, size);
//		}

		@GetMapping("/employees")
	    public List<Employee> getAllEmployees1() {
	        return employeeService.getAllEmployees();
	    }


		
		
		
	}

