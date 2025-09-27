package com.abernathy.medilabo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Document(collection = "medical_history")
public class MedicalHistoryNote {

    @Id
    private String id;

    private Long patientId;      // reference to PostgreSQL Patient
    private String physician;    // name or ID of physician
    private String note;         // the observation note
    private LocalDateTime createdAt = LocalDateTime.now();



}
