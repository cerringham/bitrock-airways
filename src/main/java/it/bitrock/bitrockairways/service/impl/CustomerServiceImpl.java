package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @Override
    public List<Customer> getAllCustomersByAge(int age) {
        return customerRepository.getCustomersByAge(age);
    }

    @Override
    public List<Customer> getAllCustomersInFidelityProgram() {
        return customerRepository.getCustomersInFidelityProgram();
    }
}
