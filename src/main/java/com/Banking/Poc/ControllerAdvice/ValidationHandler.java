package com.Banking.Poc.ControllerAdvice;

import com.Banking.Poc.Service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationHandler{
    private static final Logger logger= LogManager.getLogger(ValidationHandler.class);
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> Validation(MethodArgumentNotValidException ex){
        Map<String,String> errorMap=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        logger.error("Validation exception has been occurred while saving the data please check below for detailed exception");
        return errorMap;
    }
    @ExceptionHandler(AccNotFoundEx.class)
    public ResponseEntity<String> AccNotFoundHandler(AccNotFoundEx e){
        logger.error("Cannot find the account please check the Account Name");
        return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccBetweenDatesEx.class)
    public ResponseEntity<String> AccBetweenDatesHandler(AccBetweenDatesEx e){
        logger.error("There are no accounts in between those dates");
        return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DbEmptyEx.class)
    public ResponseEntity<String> DbemptyEx(DbEmptyEx e){
        logger.error("Accounts in the bank are empty");
        return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmptyReqEx.class)
    public ResponseEntity<String> handleEmptyReqEx(EmptyReqEx e) {

        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);

    }

}
