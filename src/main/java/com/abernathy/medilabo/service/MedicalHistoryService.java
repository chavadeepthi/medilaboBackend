package com.abernathy.medilabo.service;

import com.abernathy.medilabo.model.MedicalHistoryNote;
import com.abernathy.medilabo.repository.MedicalHistoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository repository;

    public MedicalHistoryService(MedicalHistoryRepository repository) {
        this.repository = repository;
    }

    public MedicalHistoryNote addNote(Long patientId, String physician, String note) {
        MedicalHistoryNote historyNote = new MedicalHistoryNote();
        historyNote.setPatientId(patientId);
        historyNote.setPhysician(physician);
        historyNote.setNote(note);
        return repository.save(historyNote);
    }

    public List<MedicalHistoryNote> getNotesForPatient(Long patientId) {
        return repository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
}

