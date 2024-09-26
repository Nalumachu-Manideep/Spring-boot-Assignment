package com.api_gateway.exceptions;

public class MissingAuthorizationHeaderException extends RuntimeException{
    public MissingAuthorizationHeaderException(String message) {
        super(message);
    }
}
