package com.abernathy.medilabo.service;

import com.abernathy.medilabo.exception.PatientNotFoundException;
import com.abernathy.medilabo.model.Patient;
import com.abernathy.medilabo.repository.PatientRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper; // replace PatientMapper


    public List<Patient> getAllPatients() {
        List<Patient> patients = patientRepository.findAll(); // fetch all records

        List<Patient> views = new ArrayList<>();
        for (Patient patient : patients) {
            Patient view = modelMapper.map(patient, Patient.class);
            views.add(view);
        }
        return views;
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    // PatientService
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(existingPatient -> {

                    if (updatedPatient.getFirstName() != null) {
                        existingPatient.setFirstName(updatedPatient.getFirstName());
                    }
                    if (updatedPatient.getLastName() != null) {
                        existingPatient.setLastName(updatedPatient.getLastName());
                    }
                    if (updatedPatient.getAddress() != null) {
                        existingPatient.setAddress(updatedPatient.getAddress());
                    }
                    if (updatedPatient.getPhone() != null) {
                        existingPatient.setPhone(updatedPatient.getPhone());
                    }
                    if (updatedPatient.getDob() != null) {
                        existingPatient.setDob(updatedPatient.getDob());
                    }
                    if (updatedPatient.getGender() != null) {
                        existingPatient.setGender(updatedPatient.getGender());
                    }

                    // always update updatedAt/updatedBy
                    existingPatient.setUpdatedAt(LocalDate.now());
                    existingPatient.setUpdatedBy("System");

                    return patientRepository.save(existingPatient);
                })
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    public Patient createPatient(Patient patient) {
        // Set defaults on creation
        patient.setCreatedAt(LocalDate.now());
        patient.setUpdatedAt(LocalDate.now());
        patient.setCreatedBy("admin"); // default value

        return patientRepository.save(patient);
    }

    // PatientService
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).
                orElseThrow(() -> new PatientNotFoundException(id));
        patientRepository.delete(patient);


    }

}
