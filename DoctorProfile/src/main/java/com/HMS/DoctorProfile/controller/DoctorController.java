package com.HMS.DoctorProfile.controller;


import com.HMS.DoctorProfile.dto.*;
import com.HMS.DoctorProfile.exceptions.AlreadyExistsException;
import com.HMS.DoctorProfile.exceptions.AuthenticationExceptionFound;
import com.HMS.DoctorProfile.exceptions.EmailNotFoundException;
import com.HMS.DoctorProfile.service.BindingService;
import com.HMS.DoctorProfile.service.serviceImplementation.DoctorServiceImplementation;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorServiceImplementation doctorService;
    @Autowired
    private BindingService bindingService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationRequestDTO registerRequest, BindingResult bindingResult){
        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
        if (errors != null) return errors;
        RegistrationResponseDTO registerResponse = doctorService.createDoctor(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }
    @PostMapping("/verify-doctor/{doctor_id}")
    public ResponseEntity<?> doctorVerification(@PathVariable("doctor_id") String doctor_id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.verifyDoctor(doctor_id));
        }catch (AlreadyExistsException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/disable-doctor/{doctor_id}")
    public ResponseEntity<?> disableDoctor(@PathVariable("doctor_id") String doctor_id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.disableDoctor(doctor_id));
        }catch (AlreadyExistsException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/change-availability-status/{doctor_id}")
    public ResponseEntity<?> changeStatus(@PathVariable("doctor_id") String doctor_id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.changeStatus(doctor_id));
        }catch (AlreadyExistsException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @PutMapping("/update-doctor-profile")
    public ResponseEntity<Object> updateDoctorProfile(@RequestBody UpdateRequestDto doctorProfileDto
            , BindingResult bindingResult){
        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
        if (errors != null) return errors;
        DoctorProfileDto userProfile = doctorService.updateDoctor(doctorProfileDto);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }
    @GetMapping("/get-doctor-by-email/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable("email") String email){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.getDoctorByEmail(email));
        }catch (EmailNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-by-id/{doctor_id}")
    public ResponseEntity<Object> getProfileById(@PathVariable("doctor_id") String doctor_id){
        try {
            DoctorProfileDto userProfile = doctorService.getDoctorDataById(doctor_id);
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/get-all-available-doctor")
    public ResponseEntity<Object> getDoctorByAvailability(){
        try {
            List<DoctorDto> userProfile = doctorService.getDoctorsByIsAvailable();
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/get-all-doctor-by-department/{department}")
    public ResponseEntity<Object> getDoctorByDepartment(@PathVariable("department") String department){
        try {
            List<DoctorDto> userProfile = doctorService.getDoctorsByDepartment(department);
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/create-appointment-slots")
    public ResponseEntity<Object> createAppointmentSlot(@RequestBody AppointmentSlotRequestDTO appointmentSlotRequestDTO
            , BindingResult bindingResult){
        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
        if (errors != null) return errors;
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.createSlotsFromDTO(appointmentSlotRequestDTO));
    }

}