package com.example.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.demo.entity.Employee;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(int id){
        return employeeRepository.findById(id).
                orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee updateEmployee(Employee newEmployee, int id){
        Employee employee = employeeRepository.findById(id).
                            orElseThrow(() -> new IllegalStateException(
                            "The employee id:" + id + " does not exist"));
        String name = newEmployee.getName();
        int age = newEmployee.getAge();

        if(name != null && name.length() > 0 
            && !Objects.equals(employee.getName(), name))
        {
            employee.setName(name);
        }
        
        if(age > 0 && employee.getAge() != age){
            employee.setAge(age);
        }

        employeeRepository.save(employee);
        return employee;
    } 

    public void deleteEmployee(int id){
        boolean isExisted = employeeRepository.existsById(id);
        if(!isExisted){
            throw new IllegalStateException("The employee with id:" +id+ " does not exist");
        }
        employeeRepository.deleteById(id);
    }

    public void addEmployee(Employee employee){
        Optional<Employee> employeeOptional = employeeRepository.
                                    findById(employee.getId());
        if(employeeOptional.isPresent()){
            throw new IllegalStateException("The id was duplicated");
        }
        employeeRepository.save(employee);                            
    }
}
