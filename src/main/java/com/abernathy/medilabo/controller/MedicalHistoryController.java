package com.abernathy.medilabo.controller;

import com.abernathy.medilabo.model.MedicalHistoryNote;
import com.abernathy.medilabo.service.MedicalHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/patients/history")
public class MedicalHistoryController {

    private final MedicalHistoryService service;

    public MedicalHistoryController(MedicalHistoryService service) {
        this.service = service;
    }

    // POST: /api/patients/history?patientId=5
    @PostMapping
    public MedicalHistoryNote addNote(@RequestBody MedicalHistoryNote note) {
        log.info("Adding note for Patient ID " + note.getPatientId());
        return service.addNote(note.getPatientId(), note.getPhysician(), note.getNote());
    }

    // GET: /api/patients/history?patientId=5
    @GetMapping
    public List<MedicalHistoryNote> getNotes(@RequestParam Long patientId) {
        log.info("Retrieving notes for Patient ID " + patientId);
        return service.getNotesForPatient(patientId);
    }
}

