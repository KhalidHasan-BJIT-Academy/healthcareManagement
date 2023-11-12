package com.HMS.DoctorProfile.service;

import com.HMS.DoctorProfile.dto.*;
import com.HMS.DoctorProfile.entity.DoctorEntity;
import com.HMS.DoctorProfile.exceptions.EmailNotFoundException;

import java.util.List;

public interface DoctorService {
    RegistrationResponseDTO createDoctor(RegistrationRequestDTO user) throws Exception;
    public DoctorProfileDto updateDoctor(UpdateRequestDto updateDoctor);
    DoctorDto getDoctorByEmail(String email) throws EmailNotFoundException;
    DoctorProfileDto getDoctorDataById(String id);
    DoctorEntity readByEmail(String email);
    Boolean verifyDoctor(String doctor_id);
    Boolean disableDoctor(String doctorId);
    Boolean changeStatus(String doctorId);
    List<DoctorDto> getDoctorsByIsAvailable();
    public List<DoctorDto> getDoctorsByDepartment(String department);
}
