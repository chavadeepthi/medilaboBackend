package com.abernathy.medilabo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abernathy.medilabo.controller.PatientController;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.service.PatientService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Patient patient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());          // support LocalDate
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO format

        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();

        patient = new Patient();
        patient.setPatientId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
        patient.setDob(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testGetPatient_Success() throws Exception {
        when(patientService.getPatient(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testCreatePatient() throws Exception {
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdatePatient() throws Exception {
        Patient update = new Patient();
        update.setFirstName("Jane");
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);

        mockMvc.perform(put("/api/patients?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePatient_Success() throws Exception {
        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePatient_NotFound() throws Exception {
        doThrow(new com.abernathy.medilabo.exception.PatientNotFoundException(1L))
                .when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients?id=1"))
                .andExpect(status().isNotFound());
    }
}