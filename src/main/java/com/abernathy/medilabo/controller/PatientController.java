package com.abernathy.medilabo.controller;

import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.service.PatientService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {


    private final PatientService patientService;

    // Get all patients

    @GetMapping("/all")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

     //Get a single patient by ID
    @GetMapping
    public ResponseEntity<Patient> getPatient(@RequestParam Long id) {
        try {
            Patient patient = patientService.getPatient(id);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PatientController
    @PutMapping
    public ResponseEntity<Patient> updatePatient(@RequestParam Long id, @RequestBody Patient updatedPatient) {
        try {
            Patient savedPatient = patientService.updatePatient(id, updatedPatient);
            return ResponseEntity.ok(savedPatient); // 200 OK
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @DeleteMapping
    public ResponseEntity<String> deletePatient(@RequestParam Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok("Patient Deleted Successfully"); // 200 No Content
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

    }
}
