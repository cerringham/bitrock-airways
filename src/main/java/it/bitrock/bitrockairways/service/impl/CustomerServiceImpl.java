package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.CustomerNotFoundException;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.FidelityPoints;
import it.bitrock.bitrockairways.model.dto.CustomerFidelityPointDTO;
import it.bitrock.bitrockairways.model.dto.FidelityProgramUserDTO;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.utility.FidelityProgramUserXlsExporter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
    public Optional<FidelityPoints> isCustomerInFidelityProgram(long customerID) {
        return customerRepository.getCustomerFromFidelityProgram(customerID);
    }

    @Override
    public List<CustomerFidelityPointDTO> getCustomerTotalPoints(){
        List<CustomerFidelityPointDTO> list = customerRepository.getCustomerTotalPoints();
        List<FidelityProgramUserDTO> exportList = new ArrayList<>();
        for(CustomerFidelityPointDTO customer : list){
            FidelityProgramUserDTO user = new FidelityProgramUserDTO() {
                @Override
                public String getName() {
                    return customer.getName();
                }

                @Override
                public String getSurname() {
                    return customer.getSurname();
                }

                @Override
                public String getEmail() {
                    return customer.getEmail();
                }

                @Override
                public Integer getTotalPoints() {
                    return customer.getPoints().intValue();
                }
            };
            exportList.add(user);

        }

        return list;
    }
}
