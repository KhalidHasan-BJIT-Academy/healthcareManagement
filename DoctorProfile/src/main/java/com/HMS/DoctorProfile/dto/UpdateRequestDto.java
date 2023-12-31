package com.HMS.DoctorProfile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {
    private String name;
    private String gender;
    private String department;
    private String registration_number_BDMC;
    private String allocated_room;
    private List<String> qualifications;
}