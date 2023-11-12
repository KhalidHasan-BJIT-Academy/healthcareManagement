package com.HMS.DoctorProfile.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_table")
public class DoctorEntity {
        @Id
        private String doctor_id;
        private String name;
        private String email;
        private String password;
        private String role;
        private String gender;
        private String registration_number_BDMC;
        private String department; //implement Enum
        private String allocated_room;
        private List<String> qualifications;
        private Boolean isValid;
        private LocalDate created_at;
        private Boolean isAvailable;
        private LocalTime appointmentStartTime;
        private Integer duration;
        @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
        private List<SlotEntity> slots;
}