package org.entrementes.tupan.expection;

public class UtilitiesProviderNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 8587976869058879612L;

	public UtilitiesProviderNotFoundException(String message){
		super(4042, message);
	}

}
