package org.entrementes.tupan.expection;


public class EquipmentNotFoundException extends EntityNotFoundException{

    private static final long serialVersionUID = 1L;

    public EquipmentNotFoundException(String message){
        super(4043, message);
    }
}
