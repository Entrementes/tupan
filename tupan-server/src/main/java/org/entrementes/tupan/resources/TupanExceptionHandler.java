package org.entrementes.tupan.resources;

import org.entrementes.tupan.expection.TupanException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TupanExceptionHandler {
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
		e.printStackTrace();
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(TupanException.class)
    public ResponseEntity<?> tupanException(TupanException e) {
		switch(e.getCode()){
		case NOT_FOUND:
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		case BAD_REQUEST:
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }	

}
