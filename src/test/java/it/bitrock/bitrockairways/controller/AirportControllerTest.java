package it.bitrock.bitrockairways.controller;

import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.service.impl.AirportServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AirportController.class)
public class AirportControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AirportServiceImpl airportService;

    @Test
    public void givenValidStartDate_whenGettingAirportWithMostFlights_thenStatusIsOk() throws Exception {
        LocalDate now = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders
                        .get("/airport/mostFlightsInAMonth")
                        .param("startDate", now.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoStartDate_whenGettingAirportWithMostFlights_thenStatusIsBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/airport/mostFlightsInAMonth")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}