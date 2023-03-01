package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{age}")
    public ResponseEntity<List<Customer>> getCustomersByAge(@PathVariable(value = "id") Integer age) {
        List<Customer> customers = customerService.getAllCustomersByAge(age);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/fidelityprogram")
    public ResponseEntity<List<Customer>> getCustomersInFidelityProgram() {
        List<Customer> customers = customerService.getAllCustomersInFidelityProgram();
        return ResponseEntity.ok(customers);
    }

}
