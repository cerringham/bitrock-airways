package it.bitrock.bitrockairways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.bitrock.bitrockairways.exception.CustomerNotFoundException;
import it.bitrock.bitrockairways.model.Customer;
import it.bitrock.bitrockairways.model.Ticket;
import it.bitrock.bitrockairways.service.CustomerService;
import it.bitrock.bitrockairways.service.TicketService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private TicketService ticketService;

    @BeforeAll
    static void beforeAll() {
        om.registerModule(new JavaTimeModule());
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void getTicketsByCustomerBeforeNowShouldReturnTickets() throws Exception {
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
        when(customerService.getById(CUSTOMER_ID)).thenReturn(customer);

        List<Ticket> tickets = List.of(
                buildTicket(10L, customer, "123", "15B"),
                buildTicket(15L, customer, "456", "3F"),
                buildTicket(20L, customer, "789", "5H")
        );
        when(ticketService.getTicketsByCustomerBeforeNow(customer)).thenReturn(tickets);

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customers/10/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(tickets)));

        // validate
        verify(customerService).getById(CUSTOMER_ID);
        verify(ticketService).getTicketsByCustomerBeforeNow(customer);
        verifyNoMoreInteractions(customerService, ticketService);
    }

    @Test
    void getTicketsByCustomerBeforeNowShouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
        // setup
        final long CUSTOMER_ID = 10L;
        when(customerService.getById(CUSTOMER_ID)).thenThrow(new CustomerNotFoundException(CUSTOMER_ID));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customers/10/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        // validate
        verify(customerService).getById(CUSTOMER_ID);
        verifyNoInteractions(ticketService);
        verifyNoMoreInteractions(customerService);
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
