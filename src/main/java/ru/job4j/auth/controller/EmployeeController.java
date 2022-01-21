package ru.job4j.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.EmployeeService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/employee")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final RestTemplate restTemplate;

    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    @GetMapping("/")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return new ResponseEntity<>(
                employeeService.save(employee),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{id}/account/")
    public ResponseEntity<Employee> addAccount(@PathVariable("id") int id, @RequestBody Person person) {
        Employee employee = employeeService.findById(id).orElse(null);
        if (employee == null) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NOT_FOUND
            );
        }
        Person personCreated = restTemplate.postForObject(API, person, Person.class);
        employee.addAccount(personCreated);
        employeeService.save(employee);
        return new ResponseEntity<>(
                employee,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/account/")
    public ResponseEntity<Void> updateAccount(@PathVariable("id") int id, @RequestBody Person person) {
        Employee employee = employeeService.findById(id).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        restTemplate.put(API, person);
        employee.addAccount(person);
        employeeService.save(employee);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/account/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") int id, @PathVariable("accountId") int accountId) {
        Employee employee = employeeService.findById(id).orElse(null);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        restTemplate.delete(API_ID, accountId);
        return ResponseEntity.ok().build();
    }
}
