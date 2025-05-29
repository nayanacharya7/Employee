package com.example.demo.ServiceTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.Repo.EmployeeRepo;
import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee sampleEmployee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sampleEmployee = new Employee();
        sampleEmployee.setId(1);
        sampleEmployee.setEmployee_Name("John");
        sampleEmployee.setEmployee_Age(30);
        sampleEmployee.setEmployee_Salary("50000");
    }

    @Test
    public void testAddEmployee() {
        when(employeeRepo.save(sampleEmployee)).thenReturn(sampleEmployee);
        Employee saved = employeeService.addEmployee(sampleEmployee);
        assertEquals("John", saved.getEmployee_Name());
        verify(employeeRepo, times(1)).save(sampleEmployee);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> list = Arrays.asList(sampleEmployee);
        when(employeeRepo.findAll()).thenReturn(list);
        List<Employee> result = employeeService.getEmployee();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOneEmployee_Found() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(sampleEmployee));
        Optional<Employee> result = employeeService.getOneEmployee(1);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getEmployee_Name());
    }

    @Test
    public void testGetOneEmployee_NotFound() {
        when(employeeRepo.findById(2)).thenReturn(Optional.empty());
        Optional<Employee> result = employeeService.getOneEmployee(2);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteEmployeeById_Success() {
        when(employeeRepo.existsById(1)).thenReturn(true);
        boolean result = employeeService.deleteEmployeeById(1);
        assertTrue(result);
        verify(employeeRepo).deleteById(1);
    }

    @Test
    public void testDeleteEmployeeById_Failure() {
        when(employeeRepo.existsById(2)).thenReturn(false);
        boolean result = employeeService.deleteEmployeeById(2);
        assertFalse(result);
        verify(employeeRepo, never()).deleteById(2);
    }

    @Test
    public void testUpdateEmployee_Found() {
        Employee updated = new Employee();
        updated.setEmployee_Name("Jane");
        updated.setEmployee_Age(28);
        updated.setEmployee_Salary("60000");

        when(employeeRepo.findById(1)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepo.save(any(Employee.class))).thenReturn(updated);

        Optional<Employee> result = employeeService.updateEmployee(1, updated);
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getEmployee_Name());
    }

    @Test
    public void testUpdateEmployee_NotFound() {
        when(employeeRepo.findById(2)).thenReturn(Optional.empty());
        Optional<Employee> result = employeeService.updateEmployee(2, sampleEmployee);
        assertFalse(result.isPresent());
    }
}
