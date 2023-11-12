package com.HMS.DoctorProfile.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message){
        super(message);
    }
}
