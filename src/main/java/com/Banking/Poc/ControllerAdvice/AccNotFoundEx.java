package com.Banking.Poc.ControllerAdvice;

public class AccNotFoundEx extends RuntimeException{
    public AccNotFoundEx(String m){
        super(m);
    }
}
