package com.HMS.DoctorProfile.exceptions;

public class AuthenticationExceptionFound extends RuntimeException{
    public AuthenticationExceptionFound(String message){
        super(message);
    }
}
