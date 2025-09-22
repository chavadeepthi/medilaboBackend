package com.abernathy.medilabo.controller;

import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.service.PatientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {


    private final PatientService patientService;


    /** Controller for displaying all patient records **/

    @GetMapping("/all")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }


    /** Controller for Getting one patient record with input a patient record ID **/

    @GetMapping
    public ResponseEntity<Patient> getPatient(@RequestParam Long id) {
        try {
            log.info("Retrieving Patient record with ID " + id);
            Patient patient = patientService.getPatient(id);
            return ResponseEntity.ok(patient);
        } catch (PatientNotFoundException e) {
            log.info("Error in retrieving Patient record with ID " + id);
            return ResponseEntity.notFound().build();
        }
    }


    /** Controller for Updating patient record with input a patient record ID and updated patient details**/
    @PutMapping
    public ResponseEntity<Patient> updatePatient(@RequestParam Long id, @RequestBody Patient updatedPatient) {
        try {
            Patient savedPatient = patientService.updatePatient(id, updatedPatient);
            log.info("Updated Patient record with ID " + id);
            return ResponseEntity.ok(savedPatient); // 200 OK
        } catch (PatientNotFoundException e) {
            log.info("Not able to update patient record with ID " + id);
            return ResponseEntity.notFound().build(); // 404 Not Found

        }
    }
/** Controller for creating patient record with input a patient class **/
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        log.info("Created new patient record ");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    /** Controller for Deleting patient record with input a patient record ID **/
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
