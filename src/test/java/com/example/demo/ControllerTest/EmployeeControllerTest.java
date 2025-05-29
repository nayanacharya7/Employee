package com.example.demo.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import com.example.demo.Controller.EmployeeController;
import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = new Employee();
        employee.setEmployee_Name("John Doe");
        employee.setEmployee_Age(30);
        employee.setEmployee_Salary("50000");
    }

    @Test
    public void testAddEmployee_Success() throws Exception {
        Employee employee = new Employee();
        employee.setEmployee_Name("John Doe");
        employee.setEmployee_Age(30);
        employee.setEmployee_Salary("50000");

        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/addEmpolyee") 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employee_name").value("John Doe"))
                .andExpect(jsonPath("$.employee_age").value(30))
                .andExpect(jsonPath("$.employee_salary").value("50000"));
    }


    @Test
    public void testAddEmployee_MissingName() throws Exception {
        employee.setEmployee_Name(null);

        mockMvc.perform(post("/addEmpolyee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee name is required"));
    }

    @Test
    public void testAddEmployee_MissingAge() throws Exception {
        employee.setEmployee_Age(0);

        mockMvc.perform(post("/addEmpolyee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee age is required"));
    }

    @Test
    public void testAddEmployee_MissingSalary() throws Exception {
        employee.setEmployee_Salary(null);

        mockMvc.perform(post("/addEmpolyee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee salary is required"));
    }

    @Test
    public void testGetAllEmployees_Success() throws Exception {
        // Prepare a sample Employee object
        Employee employee = new Employee();
        employee.setEmployee_Name("John Doe");
        employee.setEmployee_Age(30);
        employee.setEmployee_Salary("50000");

        // Mock the employeeService to return the list with one employee
        when(employeeService.getEmployee()).thenReturn(Arrays.asList(employee));

        // Perform GET request and validate JSON response with correct keys (snake_case)
        mockMvc.perform(get("/allEmp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name").value("John Doe"))
                .andExpect(jsonPath("$[0].employee_age").value(30))
                .andExpect(jsonPath("$[0].employee_salary").value("50000"));
    }	

    @Test
    public void testGetAllEmployees_NotFound() throws Exception {
        when(employeeService.getEmployee()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/allEmp"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No employees found"));
    }

    @Test
    public void testGetEmployeeById_Exception() throws Exception {
        // Simulate exception when service is called
        when(employeeService.getOneEmployee(1)).thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/getOneEmp/1"))
               .andExpect(status().isInternalServerError())
               .andExpect(content().string("Error retrieving employee: Database connection failed"));
    }
    
    @Test
    public void testGetEmployeeById_Found() throws Exception {
        // Prepare the Employee object
        Employee employee = new Employee();
        employee.setEmployee_Name("John Doe");
        employee.setEmployee_Age(30);
        employee.setEmployee_Salary("50000");

        // Mock the service method
        when(employeeService.getOneEmployee(1)).thenReturn(Optional.of(employee));

        // Perform GET request and verify response JSON keys (snake_case)
        mockMvc.perform(get("/getOneEmp/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("John Doe"))
                .andExpect(jsonPath("$.employee_age").value(30))
                .andExpect(jsonPath("$.employee_salary").value("50000"));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getOneEmployee(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/getOneEmp/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found with ID: 1"));
    }

    @Test
    public void testDeleteEmployee_Success() throws Exception {
        when(employeeService.deleteEmployeeById(1)).thenReturn(true);

        mockMvc.perform(delete("/deleteEmp/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully with ID 1"));
    }

    @Test
    public void testDeleteEmployee_NotFound() throws Exception {
        when(employeeService.deleteEmployeeById(1)).thenReturn(false);

        mockMvc.perform(delete("/deleteEmp/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found with ID: 1"));
    }

    @Test
    public void testPatchUpdateEmployee_Success() throws Exception {
        Employee updatedInfo = new Employee();
        updatedInfo.setEmployee_Name("Jane Smith");
        updatedInfo.setEmployee_Age(35);
        updatedInfo.setEmployee_Salary("60000");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployee_Name("Jane Smith");
        updatedEmployee.setEmployee_Age(35);
        updatedEmployee.setEmployee_Salary("60000");

        // Assuming 'employee' is a pre-existing Employee object in your test context
        when(employeeService.getOneEmployee(1)).thenReturn(Optional.of(employee));
        when(employeeService.addEmployee(any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(patch("/updateEmp/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("Jane Smith"))
                .andExpect(jsonPath("$.employee_age").value(35))
                .andExpect(jsonPath("$.employee_salary").value("60000"));
    }


    @Test
    public void testPatchUpdateEmployee_NotFound() throws Exception {
        Employee updatedInfo = new Employee();
        updatedInfo.setEmployee_Name("Jane Smith");

        when(employeeService.getOneEmployee(1)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/updateEmp/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found with ID: 1"));
    }
    
    @Test
    public void testAddEmployee_Exception() throws Exception {
        // Prepare a valid Employee object
        Employee employee = new Employee("John Doe", 30, "50000");

        // Simulate an exception thrown by the service
        when(employeeService.addEmployee(any(Employee.class)))
                .thenThrow(new RuntimeException("Database insert failed"));

        // Perform POST request and assert the error response
        mockMvc.perform(post("/addEmpolyee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while adding the employee: Database insert failed"));
    }

    @Test
    public void testGetAllEmployees_Exception() throws Exception {
        // Simulate an exception thrown by the service layer
        when(employeeService.getEmployee())
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/allEmp"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error fetching employees: Database connection failed"));
    }
    
    @Test
    public void testDeleteEmployee_Exception() throws Exception {
        // Simulate an exception thrown by the service layer
        when(employeeService.deleteEmployeeById(1))
                .thenThrow(new RuntimeException("Database error while deleting"));

        mockMvc.perform(delete("/deleteEmp/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error deleting employee: Database error while deleting"));
    }
    
    @Test
    public void testUpdateEmployeePartially_Exception() throws Exception {
        Employee partialUpdate = new Employee();
        partialUpdate.setEmployee_Name("Updated Name");

        // Simulate exception thrown by service during getOneEmployee
        when(employeeService.getOneEmployee(1)).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(patch("/updateEmp/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error updating employee: Service failure"));
    }


}

