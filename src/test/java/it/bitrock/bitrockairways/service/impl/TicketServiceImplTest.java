package it.bitrock.bitrockairways.service.impl;

import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.repository.FidelityPointsRepository;
import it.bitrock.bitrockairways.repository.TicketRepository;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {
    private TicketRepository ticketRepository;

    private CustomerService customerService;

    private FidelityPointsRepository fidelityPointsRepository;


    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        this.ticketRepository = mock(TicketRepository.class);
        this.customerService = mock(CustomerService.class);
        this.fidelityPointsRepository = mock(FidelityPointsRepository.class);
        this.ticketService = new TicketServiceImpl(ticketRepository, customerService, fidelityPointsRepository);
    }

    @Test
    void getTicketsByCustomerBeforeNowShouldReturnTickets() {
        // setup
        Customer customer = Customer.builder()
                .withId(10L)
                .withName("Name")
                .withSurname("Surname")
                .withGender("G")
                .withEmail("mail@example.com")
                .withPhone("555-123123")
                .withBirthday(LocalDate.of(1990, Month.JANUARY, 1))
                .withHandicap(false)
                .build();
        List<Ticket> tickets = List.of(
            buildTicket(10L, customer, "123", "15B"),
            buildTicket(15L, customer, "456", "3F"),
            buildTicket(20L, customer, "789", "5H")
        );

        when(ticketService.getTicketsByCustomerBeforeNow(customer)).thenReturn(tickets);

        // test
        List<Ticket> ticketsRetrieved = ticketService.getTicketsByCustomerBeforeNow(customer);

        // validate
        assertThat(ticketsRetrieved).containsExactlyInAnyOrderElementsOf(tickets);
        verify(ticketRepository).getTicketsByCustomerBeforeNow(customer);
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    void getTicketsByCustomerBeforeNowShouldThrowExceptionOnNullCustomer() {
        // test
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ticketService.getTicketsByCustomerBeforeNow(null))
                .withMessage("customer cannot be null");

        // validate
        verifyNoInteractions(ticketRepository);
    }

    private Ticket buildTicket(Long id, Customer customer, String reservationCode, String seatNumber) {
        return Ticket.builder()
                .withId(id)
                .withCustomer(customer)
                .withReservationCode(reservationCode)
                .withSeatNumber(seatNumber)
                .withDateBought(ZonedDateTime.now())
                .build();
    }
}
