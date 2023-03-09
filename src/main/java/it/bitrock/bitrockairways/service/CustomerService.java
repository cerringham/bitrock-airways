package it.bitrock.bitrockairways.service;

import it.bitrock.bitrockairways.dto.CustomerFidelityDataDTO;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.FidelityPoints;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer getById(long id);

    List<Customer> getAllCustomers();

    List<Customer> getAllCustomersByAge(int age);

    List<Customer> getAllCustomersInFidelityProgram();

    List<CustomerFidelityDataDTO> getAllFidelityCustomers();

    Optional<FidelityPoints> isCustomerInFidelityProgram(long customerID);

}
