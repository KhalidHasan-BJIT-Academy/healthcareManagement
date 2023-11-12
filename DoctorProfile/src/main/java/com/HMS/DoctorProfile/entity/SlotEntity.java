package com.HMS.DoctorProfile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_table")
public class SlotEntity {
    @Id
    @GeneratedValue
    private String slot_id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}
