package it.bitrock.bitrockairways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.model.Flight;
import it.bitrock.bitrockairways.model.Plane;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @BeforeAll
    static void beforeAll() {
        om.registerModule(new JavaTimeModule());
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void flightsBetweenDatesShouldReturnFlights() throws Exception {
        // setup
        ZonedDateTime from = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.systemDefault());
        ZonedDateTime to = ZonedDateTime.of(2023, 7, 1, 9, 0, 0, 0, ZoneId.systemDefault());
        Flight firstFlight = Flight.builder()
                .withId(1L)
                .withPlane(new Plane(1L, "Boeing 737", 300, true, null))
                .withDepartTime(from.plusDays(10))
                .withArrivalTime(from.plusHours(5))
                .build();
        Flight secondFlight = Flight.builder()
                .withId(2L)
                .withPlane(new Plane(2L, "Airbus a380", 500, true, null))
                .withDepartTime(from.plusDays(2))
                .withArrivalTime(from.plusHours(10))
                .build();
        List<Flight> flights = List.of(firstFlight, secondFlight);
        when(flightService.flightsBetweenDates(from, to)).thenReturn(flights);

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flights")
                .queryParam("from", from.format(DateTimeFormatter.ISO_DATE_TIME))
                .queryParam("to", to.format(DateTimeFormatter.ISO_DATE_TIME))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(flights)));

        // validate
        verify(flightService).flightsBetweenDates(from, to);
        verifyNoMoreInteractions(flightService);
    }

    @Test
    void flightsBetweenDatesShouldReturnBadRequestWhenFromIsMissing() throws Exception {
        // setup
        ZonedDateTime to = ZonedDateTime.of(2023, 7, 1, 9, 0, 0, 0, ZoneId.systemDefault());

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flights")
                .queryParam("to", to.format(DateTimeFormatter.ISO_DATE_TIME))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        // validate
        verifyNoInteractions(flightService);
    }

    @Test
    void flightsBetweenDatesShouldReturnBadRequestWhenToIsMissing() throws Exception {
        // setup
        ZonedDateTime from = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.systemDefault());

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flights")
                .queryParam("from", from.format(DateTimeFormatter.ISO_DATE_TIME))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        // validate
        verifyNoInteractions(flightService);
    }

    @Test
    void flightsBetweenDatesShouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // setup
        ZonedDateTime from = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.of("Europe/Rome"));
        ZonedDateTime to = ZonedDateTime.of(2023, 6, 3, 9, 0, 0, 0, ZoneId.of("Asia/Bangkok"));
        when(flightService.flightsBetweenDates(from, to)).thenThrow(new IllegalArgumentException("\"from\" must be a datetime before \"to\""));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flights")
                .queryParam("from", from.format(DateTimeFormatter.ISO_DATE_TIME))
                .queryParam("to", to.format(DateTimeFormatter.ISO_DATE_TIME))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        // validate
        verify(flightService).flightsBetweenDates(from, to);
        verifyNoMoreInteractions(flightService);
    }

    @Test
    public void givenValidFlightSearchDto_whenGettingFutureFlights_thenStatusIsOk() throws Exception {
        CustomerFlightSearchDTO flightSearchDTO = new CustomerFlightSearchDTO(1L,"MXP","NYC");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/flight/list_of_routes")
                        .content(om.writeValueAsString(flightSearchDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoFlightSearchDto_whenGettingFutureFlights_thenStatusIsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/flight/list_of_routes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

