package com.HMS.DoctorProfile.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseModel {
    private String username;
    private String bearerToken;
}
