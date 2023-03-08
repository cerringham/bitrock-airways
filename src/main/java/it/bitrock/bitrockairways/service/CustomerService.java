package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.FidelityPoints;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer getById(long id);

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersByAge(int age);

    List<Customer> getAllCustomersInFidelityProgram();

    Optional<FidelityPoints> isCustomerInFidelityProgram(long customerID);

}
