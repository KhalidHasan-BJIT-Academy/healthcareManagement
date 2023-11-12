package com.HMS.DoctorProfile.repository;


import com.HMS.DoctorProfile.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorEntity, String> {
    Optional<DoctorEntity> findByEmail(String email);
    List<DoctorEntity> findAllByIsAvailableIsTrue();
    List<DoctorEntity> findAllByDepartment(String department);
}
