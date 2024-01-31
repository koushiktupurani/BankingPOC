package com.Banking.Poc.ControllerAdvice;

public class EmptyReqEx extends RuntimeException{
    public EmptyReqEx(String m){
        super(m);
    }

}
