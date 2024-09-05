package com.abcbank.backend_test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.abcbank.backend_test.model.Employee;
import com.abcbank.backend_test.service.EmployeeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/employees")
@RestController
public class EmployeeController {

    private EmployeeService employeeService = new EmployeeService();
    
    // POST request to insert a new employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
   

        return employeeService.createEmployee(employee);
    }
    


    // Get request to get employee by ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }



    // GET request to filter employees by name, fromSalary, and toSalary
    @GetMapping
    public List<Employee> getEmployeesByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String fromSalary,
            @RequestParam(required = false) String toSalary) {

      return employeeService.getEmployeesByFilter(name, fromSalary, toSalary);

    }

}
