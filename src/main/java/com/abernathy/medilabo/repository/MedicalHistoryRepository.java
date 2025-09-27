package com.abernathy.medilabo.repository;


import com.abernathy.medilabo.model.MedicalHistoryNote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalHistoryRepository extends MongoRepository<MedicalHistoryNote, String> {
    List<MedicalHistoryNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}