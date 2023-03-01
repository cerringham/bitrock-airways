package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer getById(long id);

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersByAge(int age);

    List<Customer> getAllCustomersInFidelityProgram();

}
