package it.bitrock.bitrockairways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.bitrock.bitrockairways.exception.NoRecordException;
import it.bitrock.bitrockairways.model.Airport;
import it.bitrock.bitrockairways.model.TrafficTimeSlot;
import it.bitrock.bitrockairways.service.AirportService;
import it.bitrock.bitrockairways.service.FlightService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirportController.class)
class AirportControllerTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @MockBean
    private FlightService flightService;

    @BeforeAll
    static void beforeAll() {
        om.registerModule(new JavaTimeModule());
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void getTimeSlotsWithMostTrafficReturnsTimeSlots() throws Exception {
        // setup
        final long AIRPORT_ID = 10L;
        Airport airport = Airport.builder()
                .withId(AIRPORT_ID)
                .withName("Test airport")
                .withInternationalCode("TST")
                .build();
        when(airportService.findById(AIRPORT_ID)).thenReturn(airport);

        List<TrafficTimeSlot> timeSlots = List.of(
                new TrafficTimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0), 3, 3),
                new TrafficTimeSlot(LocalTime.of(15, 0), LocalTime.of(16, 0), 4, 2),
                new TrafficTimeSlot(LocalTime.of(22, 0), LocalTime.of(23, 0), 1, 5)
        );
        when(flightService.getTimeSlotWithMostTraffic(airport)).thenReturn(timeSlots);

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/airports/10/airport-busy-track")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(timeSlots)));

        // validate
        verify(airportService).findById(AIRPORT_ID);
        verify(flightService).getTimeSlotWithMostTraffic(airport);
        verifyNoMoreInteractions(airportService, flightService);
    }

    @Test
    void getTimeSlotsWithMostTrafficReturnsNotFoundOnNonExistingAirport() throws Exception {
        // setup
        final long AIRPORT_ID = 10L;
        when(airportService.findById(AIRPORT_ID)).thenThrow(new NoRecordException("Airport with id \"10\" does not exist"));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/airports/10/airport-busy-track")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());

        // validate
        verify(airportService).findById(AIRPORT_ID);
        verifyNoMoreInteractions(airportService, flightService);
    }

    @Test
    void getTimeSlotsWithMostTrafficReturnsBadRequestOnNullAirport() throws Exception {
        // setup
        final long AIRPORT_ID = 10L;
        when(flightService.getTimeSlotWithMostTraffic(any())).thenThrow(new IllegalArgumentException("airport cannot be null"));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/airports/10/airport-busy-track")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        // validate
        verify(airportService).findById(AIRPORT_ID);
        verify(flightService).getTimeSlotWithMostTraffic(any());
        verifyNoMoreInteractions(airportService, flightService);
    }
}
