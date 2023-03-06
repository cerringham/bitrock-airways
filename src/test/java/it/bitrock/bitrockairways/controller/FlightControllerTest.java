package it.bitrock.bitrockairways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.bitrock.bitrockairways.dto.CustomerFlightSearchDTO;
import it.bitrock.bitrockairways.service.FlightService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FlightController.class)
public class FlightControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightService flightService;

    @Test
    public void givenValidFlightSearchDto_whenGettingFutureFlights_thenStatusIsOk() throws Exception {
        CustomerFlightSearchDTO flightSearchDTO = new CustomerFlightSearchDTO(1L,"MXP","NYC");
        mvc.perform(MockMvcRequestBuilders
                        .get("/flight/list_of_routes")
                        .content(asJsonString(flightSearchDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoFlightSearchDto_whenGettingFutureFlights_thenStatusIsBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/flight/list_of_routes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}