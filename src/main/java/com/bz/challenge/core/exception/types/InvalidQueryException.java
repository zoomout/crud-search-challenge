package com.bz.challenge.core.exception.types;

public class InvalidQueryException extends RuntimeException {

    public InvalidQueryException(String details) {
        super("Invalid query: " + details);
    }
}
