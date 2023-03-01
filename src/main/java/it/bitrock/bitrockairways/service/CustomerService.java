package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersByAge(int age);

    List<Customer> getAllCustomersInFidelityProgram();

}
