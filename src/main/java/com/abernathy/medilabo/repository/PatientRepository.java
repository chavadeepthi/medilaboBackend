package com.abernathy.medilabo.repository;
import com.abernathy.medilabo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // No custom queries for now — just CRUD from JpaRepository
}
