package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.exception.CustomerNotFoundException;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.repository.CustomerRepository;
import it.bitrock.bitrockairways.repository.TicketRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {
    private CustomerService customerService;

    private CustomerRepository customerRepository;

    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        ticketRepository = mock(TicketRepository.class);
        customerService = new CustomerServiceImpl(customerRepository, ticketRepository);
    }

    @Test
    void getByIdShouldReturnCustomer() {
        // setup
        final long CUSTOMER_ID = 10L;
        Customer customer = Customer.builder()
                .withId(CUSTOMER_ID)
                .withName("Name")
                .withSurname("Surname")
                .withGender("G")
                .withEmail("mail@example.com")
                .withPhone("555-123123")
                .withBirthday(LocalDate.of(1990, Month.JANUARY, 1))
                .withHandicap(false)
                .build();
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        // test
        Customer retrievedCustomer = customerService.getById(CUSTOMER_ID);

        // validate
        assertThat(retrievedCustomer).isEqualTo(customer);
        verify(customerRepository).findById(CUSTOMER_ID);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void getByIdShouldThrowExceptionOnNonExistingCustomer() {
        // setup
        final long CUSTOMER_ID = 10L;
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        // test
        assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerService.getById(CUSTOMER_ID))
                .withMessage("customer with id \"10\" not found");

        // validate
        verify(customerRepository).findById(CUSTOMER_ID);
        verifyNoMoreInteractions(customerRepository);
    }
}
