package org.entrementes.tupan.resources;

import org.entrementes.tupan.entities.SmartAppliance;
import org.entrementes.tupan.expection.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TupanExceptionHandler {
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
		e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SmartGridNotAvailableExecption.class)
    public ResponseEntity<?> gridNotAvailable(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notFound(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SmartApplianceRegistrationConflicException.class)
    public ResponseEntity<?> conflict(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRegistrationException.class)
    public ResponseEntity<?> badRequest(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
