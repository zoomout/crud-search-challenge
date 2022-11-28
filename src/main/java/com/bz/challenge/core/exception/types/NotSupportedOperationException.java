package com.bz.challenge.core.exception.types;

public class NotSupportedOperationException extends InvalidQueryException {

    public NotSupportedOperationException(String value) {
        super("Not supported operation: " + value);
    }
}
