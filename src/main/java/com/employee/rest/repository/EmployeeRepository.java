package com.employee.rest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.employee.rest.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	 // CUSTOM METHODS
	
	 public Optional<Employee> findByEmail(String email);
	
	 public List<Employee> findBySalaryGreaterThanEqual(double minSalary);

	 public List<Employee> findBySalaryLessThanEqual(double maxSalary);

	 public List<Employee> findBySalaryBetween(double minSalary,double maxSalary);
	 
	 public List<Employee> findBySalaryGreaterThanOrGender(double salary, String gender);
	 
	 public List<Employee> findByGenderAndSalaryGreaterThan(String gender, double salary);
	 
	 public List<Employee> findByGenderOrSalaryBetween(String gender, double startSalary, double endSalary);
	            
	 public List<Employee> findAllByOrderBySalaryAsc();   
	 
	 public List<Employee> findAllByOrderBySalaryDesc();
	 
	 public List<Employee> findByNameIgnoreCase(String name);
	 
	 public List<Employee> findByNameContaining(String name);
	 
//	 @Query("select e from Employee e Where e.department = :department AND e.gender = :gender AND e.salary BETWEEN :minSalary AND :maxSalary")
//	 public List<Employee> searchEmployees(@Param("department") String department,
//			 							   @Param("gender") String gender,
//			 							   @Param("minSalary") double minSalary,
//			 							   @Param("maxSalary") double maxSalary);
	 
	 @Query("select e from Employee e Where e.department = ?1 AND e.gender = ?2 AND e.salary BETWEEN ?3 AND ?4")
	 public List<Employee> searchEmployees(String department,
			 							   String gender,
			 							   double minSalary,
			 							   double maxSalary);
	 @Transactional
	 public Optional<Employee> deleteByEmail(String email);
	           
	 @Query("delete from Employee e where e.salary BETWEEN :minSalary AND :maxSalary")
	 @Modifying
	 @Transactional
	 public void deleteEmployeeBySalaryRange(@Param ("minSalary") double minSalary,
			 								 @Param ("maxSalary") double maxSalary);  
	 
	 
}
