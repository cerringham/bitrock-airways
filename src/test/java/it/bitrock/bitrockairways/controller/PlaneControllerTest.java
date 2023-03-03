package it.bitrock.bitrockairways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.bitrock.bitrockairways.exception.InvalidPlaneQuantityException;
import it.bitrock.bitrockairways.exception.PlaneAlreadyExistsException;
import it.bitrock.bitrockairways.model.Plane;
import it.bitrock.bitrockairways.model.validation.group.OnUpdate;
import it.bitrock.bitrockairways.service.PlaneService;
import jakarta.validation.*;
import jakarta.validation.groups.Default;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaneController.class)
class PlaneControllerTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaneService planeService;

    @BeforeAll
    static void beforeAll() {
        om.registerModule(new JavaTimeModule());
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void createPlaneShouldReturnCreatedPlane() throws Exception {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(1)
                .withSeatsCount(300)
                .withActive(null);
        Plane planeInput = planeBuilder.build();
        Plane planeOutput = planeBuilder
                .withId(10L)
                .withActive(true)
                .build();
        when(planeService.create(planeInput)).thenReturn(planeOutput);

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(planeInput));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().json(om.writeValueAsString(planeOutput)));

        // validate
        verify(planeService).create(planeInput);
        verifyNoMoreInteractions(planeService);
    }

    @Test
    void createPlaneShouldReturnBadRequestOnDuplicateModel() throws Exception {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane planeInput = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(1)
                .withSeatsCount(300)
                .withActive(null)
                .build();
        when(planeService.create(planeInput)).thenThrow(new PlaneAlreadyExistsException(PLANE_MODEL));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(planeInput));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        // validate
        verify(planeService).create(planeInput);
        verifyNoMoreInteractions(planeService);
    }

    @Test
    void createPlaneShouldReturnBadRequestOnConstraintViolation() throws Exception {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane planeInput = Plane.builder()
                .withId(10L)
                .withModel(PLANE_MODEL)
                .withQuantity(1)
                .withSeatsCount(100)
                .withActive(null)
                .build();
        Set<ConstraintViolation<Plane>> violations;
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            violations = validator.validate(planeInput);
        }
        Map<Object, Object> expectedViolations = Map.of(
                "id", List.of("must be null"),
                "seatsCount", List.of("cannot be less than 200")
        );
        when(planeService.create(planeInput)).thenThrow(new ConstraintViolationException("Plane not valid", violations));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(planeInput));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(om.writeValueAsString(expectedViolations)));

        // validate
        verify(planeService).create(planeInput);
        verifyNoMoreInteractions(planeService);
    }

    @Test
    void updatePlaneShouldReturnUpdatedPlane() throws Exception {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane.PlaneBuilder planeBuilder = Plane.builder()
                .withModel(PLANE_MODEL)
                .withActive(null);
        Plane planeInput = planeBuilder
                .withQuantity(5)
                .build();
        Plane planeOutput = planeBuilder
                .withId(10L)
                .withQuantity(5)
                .withActive(true)
                .build();
        when(planeService.update(planeInput)).thenReturn(planeOutput);

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(planeInput));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(planeOutput)));

        // validate
        verify(planeService).update(planeInput);
        verifyNoMoreInteractions(planeService);
    }

    @Test
    void updatePlaneShouldReturnNotAcceptableOnInvalidPlaneQuantityException() throws Exception {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane planeInput = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(-1)
                .withActive(null)
                .build();
        when(planeService.update(planeInput)).thenThrow(new InvalidPlaneQuantityException("Plane quantity must be positive or zero"));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(planeInput));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string("Plane quantity must be positive or zero"));

        // validate
        verify(planeService).update(planeInput);
        verifyNoMoreInteractions(planeService);
    }

    @Test
    void updatePlaneShouldReturnBadRequestOnConstraintViolationException() throws Exception {
        // setup
        final String PLANE_MODEL = "Boeing 737";
        Plane planeInput = Plane.builder()
                .withModel(PLANE_MODEL)
                .withQuantity(4)
                .withSeatsCount(100)
                .withActive(null)
                .build();
        Set<ConstraintViolation<Plane>> violations;
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            violations = validator.validate(planeInput, Default.class, OnUpdate.class);
        }
        Map<Object, Object> expectedViolations = Map.of(
                "seatsCount", List.of("must be null", "cannot be less than 200")
        );

        when(planeService.update(planeInput)).thenThrow(new ConstraintViolationException("Invalid plane", violations));

        // test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(planeInput));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().json(om.writeValueAsString(expectedViolations)));

        // validate
        verify(planeService).update(planeInput);
        verifyNoMoreInteractions(planeService);
    }
}
