package com.example.demo.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Repo.EmployeeRepo;
import com.example.demo.entity.Employee;

@Service
public class EmployeeService 
{

@Autowired
private EmployeeRepo employeeRepo;
		

 	public Employee addEmployee(Employee employee) 
	{
		return	employeeRepo.save(employee);	
	}
	
	public List<Employee> getEmployee(){
		return employeeRepo.findAll();
	}
	
	public Optional<Employee> getOneEmployee(Integer id) 
	{
	    return employeeRepo.findById(id);
	}
	
	public boolean deleteEmployeeById(Integer id) {
		
	    if (!employeeRepo.existsById(id)) {
	        return false;
	    }
	    employeeRepo.deleteById(id);
	    return true;
	}
	
	public Optional<Employee> updateEmployee(Integer id, Employee updatedEmployee) {
        Optional<Employee> existingEmployeeOpt = employeeRepo.findById(id);

        if (existingEmployeeOpt.isPresent()) {
            Employee existingEmployee = existingEmployeeOpt.get();
            existingEmployee.setEmployee_Name(updatedEmployee.getEmployee_Name());
            existingEmployee.setEmployee_Age(updatedEmployee.getEmployee_Age());
            existingEmployee.setEmployee_Salary(updatedEmployee.getEmployee_Salary());

            Employee saved = employeeRepo.save(existingEmployee);
            return Optional.of(saved);
        } else {
            return Optional.empty();
        }
    }
}
