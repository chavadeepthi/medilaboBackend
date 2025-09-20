package com.abernathy.medilabo;

import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.repository.PatientRepository;
import com.abernathy.medilabo.service.PatientService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatientService patientServiceMock;

    public Patient patient;

    private List<Patient> patients;
    private List<Patient> patientDTOs;

    @BeforeEach

    void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a sample patient
        patient = new Patient();
        patient.setPatientId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
        patient.setDob(LocalDate.of(1990, 1, 1));

        patients = Arrays.asList(
                new Patient(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "M", "123 Main St", "555-1234",
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1), "System", "System"),
                new Patient(2L, "Jane", "Smith", LocalDate.of(1985, 5, 15), "M","456 Oak Ave", "555-5678",
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1), "System", "System")
        );



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

    @Test
    void testGetAllPatients() {
        // Mock repository behavior
        when(patientRepositoryMock.findAll()).thenReturn(patients);

        // Mock ModelMapper behavior to return the same Patient entity
        when(modelMapper.map(any(), eq(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the service method
        List<Patient> result = patientServiceMock.getAllPatients();

        // Verify interactions
        verify(patientRepositoryMock).findAll();
        verify(modelMapper, times(patients.size())).map(any(), eq(Patient.class));

        // Assert the result
        assertNotNull(result);
        assertEquals(patients.size(), result.size());
        assertEquals(patients.get(0).getLastName(), result.get(0).getLastName());
        assertEquals(patients.get(1).getAddress(), result.get(1).getAddress());
    }

    @Test
    void testUpdatePatient_Success() {
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Jane");

        when(patientRepositoryMock.findById(eq(1L))).thenReturn(java.util.Optional.of(patient));
        when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientServiceMock.updatePatient(1L, updatedPatient);

        verify(patientRepositoryMock, times(1)).save(any(Patient.class));
        assert result.getFirstName().equals("Jane");
    }

//    @Test
//    void testUpdatePatient_NotFound() {
//        Patient updatedPatient = new Patient();
//        updatedPatient.setFirstName("Jane");
//
//        when(patientRepositoryMock.findById(eq(1L))).thenReturn(java.util.Optional.empty());
//
//        try {
//            patientServiceMock.updatePatient(1L, updatedPatient);
//        } catch (PatientNotFoundException e) {
//            assert e.getMessage().equals("Patient not found with ID: 1");
//        }
//    }

}
