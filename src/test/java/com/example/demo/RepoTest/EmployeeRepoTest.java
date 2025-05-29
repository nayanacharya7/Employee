package com.example.demo.RepoTest;
import com.example.demo.Repo.EmployeeRepo;
import com.example.demo.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepoTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Test
    public void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setEmployee_Name("John Doe");
        employee.setEmployee_Age(30);
        employee.setEmployee_Salary("50000");

        Employee savedEmployee = employeeRepo.save(employee);
        System.out.println("savedEmployee.getId() : "+savedEmployee.getId());
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
    }

    @Test
    public void testFindById() {
        Employee employee = new Employee();
        employee.setEmployee_Name("Jane Doe");
        employee.setEmployee_Age(28);
        employee.setEmployee_Salary("60000");

        Employee saved = employeeRepo.save(employee);

        Optional<Employee> found = employeeRepo.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmployee_Name()).isEqualTo("Jane Doe");
    }

    @Test
    public void testDeleteById() {
        Employee employee = new Employee();
        employee.setEmployee_Name("Bob Smith");
        employee.setEmployee_Age(40);
        employee.setEmployee_Salary("70000");

        Employee saved = employeeRepo.save(employee);

        employeeRepo.deleteById(saved.getId());

        Optional<Employee> deleted = employeeRepo.findById(saved.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    public void testFindAll() {
        employeeRepo.save(new Employee("A", 25, "45000"));
        employeeRepo.save(new Employee("B", 26, "46000"));

        assertThat(employeeRepo.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }
}

