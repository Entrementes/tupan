package org.entrementes.tupan.expection;

public class ServiceVersionNotFound extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceVersionNotFound(String message){
        super(message);
    }

}
