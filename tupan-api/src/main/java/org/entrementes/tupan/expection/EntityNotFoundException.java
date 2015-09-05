package org.entrementes.tupan.expection;

public class EntityNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private final int errorCode;

    public EntityNotFoundException(int errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
