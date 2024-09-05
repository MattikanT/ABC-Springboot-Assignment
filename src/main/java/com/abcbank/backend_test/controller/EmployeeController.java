package com.abcbank.backend_test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.abcbank.backend_test.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/employees")
@RestController
public class EmployeeController {

    // Path to the JSON file where employee data is stored
    private static final String JSON_FILE_PATH = "employees.json";

    // A list to store employees in memory during runtime
    private List<Employee> employees = new ArrayList<>();

    public EmployeeController() {
        // When the application starts, load employees from the JSON file
        this.employees = readEmployeesFromFile();
        // Sample employee data
        // this.employees = new ArrayList<>();
        // employees.add(new Employee(1, "Alice", "Ahmed", "2000-01-01", "7000", "2023-05-18", "IT"));
        // employees.add(new Employee(2, "Mail", "Tongsai", "2000-01-01", "5000", "2023-05-18", "IT"));
        // employees.add(new Employee(3, "Eslam", "Allam", "2000-01-01", "1000", "2023-05-18", "IT"));
    }

    
    // POST request to insert a new employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
   
        int newId = employees.stream()
                .mapToInt(Employee::getId) 
                .max() 
                .orElse(0) + 1; 

        employee.setId(newId);
        employees.add(employee);
        writeEmployeesToFile(employees);
        
        return employee;
    }
    

    // Get request to get employee by ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Integer id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }



    // GET request to filter employees by name, fromSalary, and toSalary
    @GetMapping
    public List<Employee> getEmployeesByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String fromSalary,
            @RequestParam(required = false) String toSalary) {

        return employees.stream()
                .filter(employee -> (name == null || employee.getFirstName().toLowerCase().contains(name.toLowerCase()) 
                                    || employee.getLastName().toLowerCase().contains(name.toLowerCase())))// Filter by name
                .filter(employee -> (fromSalary == null
                        || Double.parseDouble(employee.getSalary()) >= Double.parseDouble(fromSalary))) // Filter by
                                                                                                        // fromSalary
                .filter(employee -> (toSalary == null
                        || Double.parseDouble(employee.getSalary()) <= Double.parseDouble(toSalary))) // Filter by
                                                                                                      // toSalary
                .collect(Collectors.toList()); // Collect filtered list

    }




        // Helper methods to read/write JSON
    private void writeEmployeesToFile(List<Employee> users) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(JSON_FILE_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Employee> readEmployeesFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(JSON_FILE_PATH),
                    new TypeReference<List<Employee>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    

}
