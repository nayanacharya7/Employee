package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/addEmpolyee")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
	    try {
	        if (employee.getEmployee_Name() == null || employee.getEmployee_Name().isEmpty()) {
	            return new ResponseEntity<>("Employee name is required", HttpStatus.BAD_REQUEST);
	        }
	        if (employee.getEmployee_Age() == 0) {
	            return new ResponseEntity<>("Employee age is required", HttpStatus.BAD_REQUEST);
	        }
	        if (employee.getEmployee_Salary() == null || employee.getEmployee_Salary().isEmpty()) {
	            return new ResponseEntity<>("Employee salary is required", HttpStatus.BAD_REQUEST);
	        }

	        Employee savedEmployee = employeeService.addEmployee(employee);
	        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);

	    } catch (Exception e) {
	        return new ResponseEntity<>("An error occurred while adding the employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@GetMapping("/allEmp")
	public ResponseEntity<?> getAllEmployees() {
	    try {
	        List<Employee> employees = employeeService.getEmployee();
	        if (employees.isEmpty()) {
	            return new ResponseEntity<>("No employees found", HttpStatus.NOT_FOUND);
	        } else {
	            return new ResponseEntity<>(employees, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error fetching employees: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/getOneEmp/{id}")
	public ResponseEntity<?> getEmployee(@PathVariable Integer id) {
	    try {
	        Optional<Employee> employee = employeeService.getOneEmployee(id);
	        if (employee.isPresent()) {
	            return new ResponseEntity<>(employee.get(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error retrieving employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@DeleteMapping("/deleteEmp/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {
	    try {
	        boolean deleted = employeeService.deleteEmployeeById(id);
	        if (deleted) {
	            return new ResponseEntity<>("Employee deleted successfully with ID " + id , HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Error deleting employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PatchMapping("/updateEmp/{id}")
	public ResponseEntity<?> updateEmployeePartially(@PathVariable Integer id, @RequestBody Employee employee) {
	    try {
	        Optional<Employee> existingEmployeeOpt = employeeService.getOneEmployee(id);
	        if (existingEmployeeOpt.isEmpty()) {
	            return new ResponseEntity<>("Employee not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }

	        Employee existingEmployee = existingEmployeeOpt.get();

	        if (employee.getEmployee_Name() != null && !employee.getEmployee_Name().isEmpty()) {
	            existingEmployee.setEmployee_Name(employee.getEmployee_Name());
	        }

	        if (employee.getEmployee_Age() != 0) {
	            existingEmployee.setEmployee_Age(employee.getEmployee_Age());
	        }

	        if (employee.getEmployee_Salary() != null && !employee.getEmployee_Salary().isEmpty()) {
	            existingEmployee.setEmployee_Salary(employee.getEmployee_Salary());
	        }

	        Employee updated = employeeService.addEmployee(existingEmployee);
	        return new ResponseEntity<>(updated, HttpStatus.OK);

	    } catch (Exception e) {
	        return new ResponseEntity<>("Error updating employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
