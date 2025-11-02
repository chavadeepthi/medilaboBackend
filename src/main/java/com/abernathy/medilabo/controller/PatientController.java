package com.abernathy.medilabo.controller;


import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/all")
    public List<Patient> getAllPatients() {
        log.info("Fetching all patient records.");
        List<Patient> patients = patientService.getAllPatients();
        log.info("Retrieved {} patient records.", patients.size());
        return patients;
    }

    @GetMapping
    public ResponseEntity<Patient> getPatient(@RequestParam Long id) {
        log.info("Attempting to retrieve patient with ID: {}", id);
        try {
            Patient patient = patientService.getPatient(id);
            log.info("Successfully retrieved patient with ID: {}", id);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            log.error("Patient with ID {} not found.", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        log.info("Creating new patient record: {}", patient);
        Patient savedPatient = patientService.createPatient(patient);
        log.info("Successfully created patient record with ID: {}");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @PutMapping
    public ResponseEntity<Patient> updatePatient(@RequestParam Long id, @RequestBody Patient updatedPatient) {
        log.info("Attempting to update patient with ID: {}", id);
        try {
            Patient savedPatient = patientService.updatePatient(id, updatedPatient);
            log.info("Successfully updated patient with ID: {}", id);
            return ResponseEntity.ok(savedPatient);
        } catch (PatientNotFoundException e) {
            log.error("Patient with ID {} not found for update.", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deletePatient(@RequestParam Long id) {
        log.info("Attempting to delete patient with ID: {}", id);
        try {
            patientService.deletePatient(id);
            log.info("Successfully deleted patient with ID: {}", id);
            return ResponseEntity.ok("Patient deleted successfully.");
        } catch (PatientNotFoundException e) {
            log.error("Patient with ID {} not found for deletion.", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

