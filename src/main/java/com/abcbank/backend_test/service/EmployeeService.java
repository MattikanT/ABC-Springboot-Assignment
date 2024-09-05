package com.abcbank.backend_test.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.abcbank.backend_test.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeService {
    
    private static final String JSON_FILE_PATH = "employees.json";

    private List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        this.employees = readEmployeesFromFile();
  
    }

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
    

    // Insert a new employee
    public Employee createEmployee( Employee employee){
        int newId = employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;

        employee.setId(newId);
        employees.add(employee);
        writeEmployeesToFile(employees);

        return employee;

    }

    // Get employee by ID
    public Employee getEmployeeById(Integer id){
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    // Filter employees by name, fromSalary, and toSalary
    public List<Employee> getEmployeesByFilter( String name, String fromSalary, String toSalary ){

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

}
