package com.HMS.DoctorProfile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlotResponseDTO {
    @NotNull
    private String doctor_id;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private Boolean isAvailable;
}
