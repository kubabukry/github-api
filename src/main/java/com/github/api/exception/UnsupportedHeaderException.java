package com.github.api.exception;

public class UnsupportedHeaderException extends RuntimeException{
    public UnsupportedHeaderException(String message){
        super(message);
    }
}
