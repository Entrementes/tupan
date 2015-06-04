package org.entrementes.tupan.expection;

public class TupanException extends RuntimeException {

	private static final long serialVersionUID = -5897123633958228061L;
	
	private final TupanExceptionCode code;
	
	public TupanException(TupanExceptionCode code) {
		this.code = code;
	}
	
	public TupanExceptionCode getCode() {
		return code;
	}

}
