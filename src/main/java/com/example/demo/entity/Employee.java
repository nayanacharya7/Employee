package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("employee_name")
    private String employee_Name;

    @JsonProperty("employee_age")
    private int employee_Age;

    @JsonProperty("employee_salary")
    private String employee_Salary;
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmployee_Name() {
		return employee_Name;
	}
	public void setEmployee_Name(String employee_Name) {
		this.employee_Name = employee_Name;
	}
	public int getEmployee_Age() {
		return employee_Age;
	}
	public void setEmployee_Age(int employee_Age) {
		this.employee_Age = employee_Age;
	}
	public String getEmployee_Salary() {
		return employee_Salary;
	}
	public void setEmployee_Salary(String employee_Salary) {
		this.employee_Salary = employee_Salary;
	}
	public Employee(String employee_Name, int employee_Age, String employee_Salary) {
	    this.employee_Name = employee_Name;
	    this.employee_Age = employee_Age;
	    this.employee_Salary = employee_Salary;
	}
	
	
	
}
