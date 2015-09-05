package org.entrementes.tupan.expection;


public class InvalidRegistrationException extends RuntimeException{

    public InvalidRegistrationException(String message){
        super(message);
    }
}
