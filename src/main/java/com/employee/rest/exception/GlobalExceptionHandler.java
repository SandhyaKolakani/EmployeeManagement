package com.employee.rest.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleNotFound(EmployeeNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							 .header("Info", "Employee Not Found")
							 .body(ex.getMessage());
	}

	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> validationExceptionHandling(
	            MethodArgumentNotValidException ex) {

	        Map<String, String> errors = new HashMap<>();

	        ex.getBindingResult()
	          .getAllErrors()
	          .forEach(error -> {

	              String fieldName = ((FieldError) error).getField();
	              String message = error.getDefaultMessage();

	              errors.put(fieldName, message);
	          });

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .header("Info", "Employee Data is not in correct format")
	                .body(errors);
	    }

}
