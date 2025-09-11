package com.abernathy.medilabo;

import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.repository.PatientRepository;
import com.abernathy.medilabo.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;

    @InjectMocks
    private PatientService patientServiceMock;

    public Patient patient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        patient = new Patient();
        patient.setPatientId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
        patient.setDob(LocalDate.of(1990, 1, 1));
    }
    @Test
    void testGetPatient_Success() {
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientServiceMock.getPatient(1L);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetPatient_NotFound() {
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientServiceMock.getPatient(1L));
    }
    @Test
    void testCreatePatient() {
        when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientServiceMock.createPatient(patient);
        assertEquals("John", result.getFirstName());
        assertNotNull(result.getCreatedAt());
        assertEquals("admin", result.getCreatedBy());
    }

    @Test
    void testUpdatePatient_Partial() {
        Patient update = new Patient();
        update.setFirstName("Jane");

        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientServiceMock.updatePatient(1L, update);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName()); // unchanged
    }

    @Test
    void testDeletePatient_Success() {
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.of(patient));
        doNothing().when(patientRepositoryMock).delete(patient);

        assertDoesNotThrow(() -> patientServiceMock.deletePatient(1L));
    }

    @Test
    void testDeletePatient_NotFound() {
        when(patientRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientServiceMock.deletePatient(1L));
    }

}
