package com.HMS.DoctorProfile.service.serviceImplementation;

import com.HMS.DoctorProfile.dto.*;
import com.HMS.DoctorProfile.entity.DoctorEntity;
import com.HMS.DoctorProfile.entity.SlotEntity;
import com.HMS.DoctorProfile.exceptions.AlreadyExistsException;
import com.HMS.DoctorProfile.exceptions.CustomException;
import com.HMS.DoctorProfile.exceptions.EmailNotFoundException;
import com.HMS.DoctorProfile.exceptions.ResourceNotFoundException;
import com.HMS.DoctorProfile.repository.DoctorRepository;
import com.HMS.DoctorProfile.repository.SlotRepository;
import com.HMS.DoctorProfile.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImplementation implements DoctorService, UserDetailsService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SlotRepository slotRepository;

    @Override
    public RegistrationResponseDTO createDoctor(RegistrationRequestDTO doctor) {
        ModelMapper modelMapper = new ModelMapper();
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent())
            throw new AlreadyExistsException("Email already exists");
        DoctorEntity doctorProfile = new DoctorEntity();
        doctorProfile.setDoctor_id(UUID.randomUUID().toString());
        doctorProfile.setName(doctor.getName());
        doctorProfile.setEmail(doctor.getEmail());
        doctorProfile.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));
        doctorProfile.setRole("DOCTOR");
        doctorProfile.setGender(doctor.getGender());
        doctorProfile.setDepartment(doctor.getDepartment());
        doctorProfile.setRegistration_number_BDMC(doctor.getRegistration_number_BDMC());
        doctorProfile.setAllocated_room(doctor.getAllocated_room());
        doctorProfile.setQualifications(doctor.getQualifications());
        doctorProfile.setIsValid(false);
        doctorProfile.setIsAvailable(false);
        doctorProfile.setCreated_at(LocalDate.now());
        DoctorEntity storedUserDetails = doctorRepository.save(doctorProfile);
        return modelMapper.map(storedUserDetails, RegistrationResponseDTO.class);
    }

    @Override
    public DoctorDto getDoctorByEmail(String email) throws EmailNotFoundException {
        if (doctorRepository.findByEmail(email).isEmpty())
            throw new EmailNotFoundException("No Doctor is found by this email");
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email).get();
        DoctorDto returnValue = new DoctorDto();
        returnValue.setDoctor_id(doctorEntity.getDoctor_id());
        BeanUtils.copyProperties(doctorEntity, returnValue);
        return returnValue;
    }
    @Override
    public DoctorEntity readByEmail(String email) {
        return doctorRepository.findByEmail(email).get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(doctorRepository.findByEmail(email).isEmpty())
            System.out.println("No user Found");
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email).get();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(doctorEntity.getRole()));
        System.out.println("Role: "+roles);
        if (doctorEntity == null) throw new UsernameNotFoundException(email);
        return new User(doctorEntity.getEmail(), doctorEntity.getPassword(),
                true, true, true, true,
                roles);
    }

    @Override
    public DoctorProfileDto getDoctorDataById(String doctor_id) {

        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctor_id);
        if(doctorProfile.isEmpty()){
            System.out.println("No user found khalid\n\n\n\n");
        }

        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            return new ModelMapper().map(doctorProfileEntity, DoctorProfileDto.class);
        } else {
            throw new ResourceNotFoundException("User profile not found by id");
        }
    }

    @Override
    public DoctorProfileDto updateDoctor(UpdateRequestDto updateDoctor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<DoctorEntity> user = doctorRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            throw new UsernameNotFoundException("No user found");
        String userId = user.get().getDoctor_id();

        DoctorEntity existingDoctorProfile = doctorRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Profile Not Found For User ID: " + userId));

        System.out.println("Doctor ID: " + existingDoctorProfile.getDoctor_id());
        System.out.println("Qualifications: " + existingDoctorProfile.getQualifications());

        List<String> qualifications = new ArrayList<>(existingDoctorProfile.getQualifications());
        System.out.println("Qualification clone list: " + qualifications);

        qualifications.addAll(updateDoctor.getQualifications());
        existingDoctorProfile.setName(updateDoctor.getName());
        existingDoctorProfile.setGender(updateDoctor.getGender());
        existingDoctorProfile.setDepartment(updateDoctor.getDepartment());
        existingDoctorProfile.setRegistration_number_BDMC(updateDoctor.getRegistration_number_BDMC());
        existingDoctorProfile.setQualifications(qualifications);

        System.out.println("Doctor ID: " + existingDoctorProfile.getDoctor_id());
        System.out.println("Qualifications: " + existingDoctorProfile.getQualifications());

        doctorRepository.save(existingDoctorProfile);
        return new ModelMapper().map(existingDoctorProfile, DoctorProfileDto.class);
    }

    @Override
    public Boolean verifyDoctor(String doctor_id){
        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctor_id);
        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            if(doctorProfileEntity.getIsValid()==true)
                throw new AlreadyExistsException("The Doctor is Already Verified");
            doctorProfileEntity.setIsValid(true);
            doctorProfileEntity.setIsAvailable(true);
            doctorRepository.save(doctorProfileEntity);
            return true;
        } else {
            throw new ResourceNotFoundException("Unable to verify doctor");
        }
    }
    @Override
    public Boolean disableDoctor(String doctorId) {
        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctorId);
        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            if(!doctorProfileEntity.getIsValid())
                throw new AlreadyExistsException("The Doctor is Already been Disable");
            doctorProfileEntity.setIsValid(false);
            doctorProfileEntity.setIsAvailable(false);
            doctorRepository.save(doctorProfileEntity);
            return true;
        } else {
            throw new ResourceNotFoundException("Unable to disable doctor with  doctorId: " + doctorProfile.get().getDoctor_id());
        }
    }
    @Override
    public Boolean changeStatus(String doctorId) {
        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctorId);
        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            if(doctorProfileEntity.getIsAvailable()){
                doctorProfileEntity.setIsAvailable(false);
            }else doctorProfileEntity.setIsAvailable(true);
            doctorRepository.save(doctorProfileEntity);
            return true;
        }else {
            throw new ResourceNotFoundException("Unable to change the status of the doctor");
        }
    }
    @Override
    public List<DoctorDto> getDoctorsByIsAvailable() {
        List<DoctorEntity> doctorEntityList = doctorRepository.findAllByIsAvailableIsTrue();
        if (doctorEntityList!=null) {
            ModelMapper modelMapper = new ModelMapper();
            List<DoctorDto> doctorDtoList = doctorEntityList.stream()
                    .map(doctorEntity -> modelMapper.map(doctorEntity, DoctorDto.class))
                    .collect(Collectors.toList());
            return doctorDtoList;
        } else {
            throw new ResourceNotFoundException("Unable to Find Any Doctor");
        }
    }

    public List<DoctorDto> getDoctorsByDepartment(String department) {
        List<DoctorEntity> doctorEntityList = doctorRepository.findAllByDepartment(department);
        if (doctorEntityList!=null) {
            ModelMapper modelMapper = new ModelMapper();
            List<DoctorDto> doctorDtoList = doctorEntityList.stream()
                    .map(doctorEntity -> modelMapper.map(doctorEntity, DoctorDto.class))
                    .collect(Collectors.toList());
            return doctorDtoList;
        } else {
            throw new ResourceNotFoundException("Unable to Find Any Doctor");
        }
    }
    public List<AppointmentSlotResponseDTO> createSlotsFromDTO(AppointmentSlotRequestDTO slotDTO) throws CustomException {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<DoctorEntity> doctor = doctorRepository.findByEmail(authentication.getName());

            if (doctor.isPresent()) {
                String doctorId = doctor.get().getDoctor_id();

                LocalDateTime startTime = slotDTO.getStartTime();
                int totalDurationMinutes = slotDTO.getDuration();

                List<AppointmentSlotResponseDTO> appointmentSlots= new ArrayList<>();
                ModelMapper modelMapper = new ModelMapper();
                // Creating slots with a fixed duration of 20 minutes
                while (startTime.plusMinutes(20).isBefore(startTime.plusMinutes(totalDurationMinutes))) {
                    SlotEntity slot = new SlotEntity();
                    slot.setDoctor(doctor.get()); // Assuming doctorId is a Long in SlotEnt
                    // ity
                    slot.setStartTime(startTime.toLocalTime());
                    slot.setEndTime(startTime.plusMinutes(20).toLocalTime());
                    slot.setIsAvailable(true); // You may set this based on your logic
                    appointmentSlots.add(modelMapper.map(slot, AppointmentSlotResponseDTO.class));
                    slotRepository.save(slot);
                    startTime = startTime.plusMinutes(20);
                }
                return appointmentSlots;
            } else {
                throw new CustomException("Doctor not found");
            }
        } catch (Exception e) {
            throw new CustomException("An error occurred while creating slots");
        }
    }
}