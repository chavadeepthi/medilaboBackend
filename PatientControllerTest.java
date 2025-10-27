package com.abernathy.medilabo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abernathy.medilabo.controller.PatientController;
import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.service.PatientService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = PatientController.class,
        excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private ObjectMapper objectMapper;

    private Patient patient;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // support LocalDate
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO format

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
    void testGetPatient_NotFound() throws Exception {
        when(patientService.getPatient(1L)).thenThrow(new PatientNotFoundException(1L));

        mockMvc.perform(get("/api/patients")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
    void testUpdatePatient_NotFound() throws Exception {
        when(patientService.updatePatient(eq(1L), any(Patient.class)))
                .thenThrow(new PatientNotFoundException(1L));

        mockMvc.perform(put("/api/patients?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Patient())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePatient_Success() throws Exception {
        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePatient_NotFound() throws Exception {
        doThrow(new PatientNotFoundException(1L))
                .when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients?id=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllPatients() throws Exception {
        List<Patient> patients = Arrays.asList(
                new Patient(1L, "Doe", "John", LocalDate.of(1990, 1, 1), "M", "123 Main St", "555-1234",
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1), "System", "System"),
                new Patient(2L, "Smith", "Jane", LocalDate.of(1985, 5, 15), "F", "456 Oak Ave", "555-5678",
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1), "System", "System")
        );

        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/api/patients/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(patients.size()))
                .andExpect(jsonPath("$[0].patientId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[1].patientId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].address").value("456 Oak Ave"));
    }
}
