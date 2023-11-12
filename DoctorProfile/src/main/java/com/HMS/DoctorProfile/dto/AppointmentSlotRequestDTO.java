package com.HMS.DoctorProfile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlotRequestDTO {
    @NotNull(message = "Start time can not be blank")
    private LocalDateTime startTime;
    @NotNull(message = "Total duration is needed to be specified")
    private Integer duration;
}
