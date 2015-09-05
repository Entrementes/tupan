package org.entrementes.tupan.expection;

public class UserNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = -1921426174468676811L;

	public UserNotFoundException(String message){
		super(40041, message);
	}

}
