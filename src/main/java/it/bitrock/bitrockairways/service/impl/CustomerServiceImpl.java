package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.dto.CustomerFidelityDataDTO;
import it.bitrock.bitrockairways.exception.CustomerNotFoundException;
import it.bitrock.bitrockairways.exception.CustomersFileWriteException;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.FidelityPoints;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.TicketRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.utility.ExcelWriter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, TicketRepository ticketRepository) {
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Customer getById(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(id);
        }
        return customer.get();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @Override
    public List<Customer> getAllCustomersByAge(int age) {
        LocalDate birthDate = LocalDate.now().minusYears(age);
        return customerRepository.getCustomersWithBirthDateLessThanOrEqual(birthDate);
    }

    @Override
    public List<Customer> getAllCustomersInFidelityProgram() {
        return customerRepository.getCustomersInFidelityProgram();
    }

    @Override
    public List<CustomerFidelityDataDTO> getAllFidelityCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerFidelityDataDTO> fidelityDataDTO = new ArrayList<>();
        for (Customer customer : customers) {
            List<Ticket> tickets = ticketRepository.findAllByCustomerId(customer.getId());
            int totalPoints = tickets.size() * 100;
            fidelityDataDTO.add(new CustomerFidelityDataDTO(customer.getId(), customer.getName(), customer.getSurname(), customer.getEmail(), totalPoints));
        }
        List<CustomerFidelityDataDTO> fidelityCustomers = fidelityDataDTO.stream()
                .sorted(Comparator.comparingInt(CustomerFidelityDataDTO::getTotalPoints).reversed())
                .collect(Collectors.toList());
        try {
            ExcelWriter.writeCustomersToExcel(fidelityCustomers, Path.of("/tmp", "most-fidelity-customer.xls"));
        } catch (IOException e) {
            throw new CustomersFileWriteException("Can't write customers file. Cause: " + e.getMessage());
        }
        return fidelityCustomers;
    }

    @Override
    public Optional<FidelityPoints> isCustomerInFidelityProgram(long customerID) {
        return customerRepository.getCustomerFromFidelityProgram(customerID);
    }
}
