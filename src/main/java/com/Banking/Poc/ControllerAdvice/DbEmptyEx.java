package com.Banking.Poc.ControllerAdvice;

public class DbEmptyEx extends RuntimeException{
    public DbEmptyEx(String message){
        super(message);
    }
}
