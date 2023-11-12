package com.HMS.DoctorProfile.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
