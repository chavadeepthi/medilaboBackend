package com.abernathy.medilabo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.abernathy.medilabo.model.MedicalHistoryNote;
import com.abernathy.medilabo.repository.MedicalHistoryRepository;
import com.abernathy.medilabo.service.MedicalHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MedicalNotesServiceTest {

    private MedicalHistoryRepository repository;
    private MedicalHistoryService service;

    @BeforeEach
    void setUp() {
        repository = mock(MedicalHistoryRepository.class);
        service = new MedicalHistoryService(repository);
    }

    @Test
    void addNote_shouldPopulateAndSave() {
        Long patientId = 123L;
        String physician = "Dr. Smith";
        String note = "Patient is recovering";
        String patientName = "John Doe";

        // Create a dummy return value — e.g. the repository returns the same object (or maybe with ID populated)
        MedicalHistoryNote savedNote = new MedicalHistoryNote();
        savedNote.setPatientId(patientId);
        savedNote.setPhysician(physician);
        savedNote.setNote(note);
        savedNote.setPatientName(patientName);
        // You might set other fields (like createdAt, or ID) as the repository would in a real scenario
        when(repository.save(any(MedicalHistoryNote.class))).thenReturn(savedNote);

        MedicalHistoryNote result = service.addNote(patientId, physician, note, patientName);

        // Verify repository save was called once, and capture the argument to inspect it
        verify(repository, times(1)).save(any(MedicalHistoryNote.class));

        // Assertions about the result
        assertNotNull(result);
        assertEquals(patientId, result.getPatientId());
        assertEquals(physician, result.getPhysician());
        assertEquals(note, result.getNote());
        assertEquals(patientName, result.getPatientName());
        // If the repo added other fields (like ID), you can assert those too if your dummy return value had them
    }

    @Test
    void getNotesForPatient_shouldDelegateToRepository() {
        Long patientId = 456L;
        // Prepare some dummy list
        MedicalHistoryNote n1 = new MedicalHistoryNote();
        n1.setPatientId(patientId);
        n1.setNote("Note A");
        MedicalHistoryNote n2 = new MedicalHistoryNote();
        n2.setPatientId(patientId);
        n2.setNote("Note B");
        List<MedicalHistoryNote> dummyList = Arrays.asList(n1, n2);

        when(repository.findByPatientIdOrderByCreatedAtDesc(patientId)).thenReturn(dummyList);

        List<MedicalHistoryNote> result = service.getNotesForPatient(patientId);

        // Verify repository method was called
        verify(repository, times(1)).findByPatientIdOrderByCreatedAtDesc(patientId);
        // Assert the returned list is what the mock returned
        assertSame(dummyList, result);
    }

    @Test
    void addNote_whenRepositoryReturnsNull_shouldReturnNull() {
        // In case repository.save returns null (unexpected), service should pass that through (or you might change behavior)
        when(repository.save(any(MedicalHistoryNote.class))).thenReturn(null);

        MedicalHistoryNote result = service.addNote(1L, "Dr", "note", "Name");

        verify(repository, times(1)).save(any(MedicalHistoryNote.class));
        assertNull(result);
    }
}

