package com.HMS.DoctorProfile.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/doctor/register", "/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/doctor/verify-doctor/{doctor_id}").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.POST,"/doctor/change-availability-status/{doctor_id}").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.PUT,"/doctor/update-doctor-profile").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/get-by-id/{doctor_id}").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/get-doctor-by-email/{email}").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/get-all-available-doctor").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/doctor/get-all-doctor-by-department/{department}").hasAuthority("DOCTOR")
                        .requestMatchers(HttpMethod.POST,"/doctor/create-appointment-slots").hasAuthority("DOCTOR")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}