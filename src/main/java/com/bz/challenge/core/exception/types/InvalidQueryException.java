package com.bz.challenge.core.exception.types;

/**
 * Exception thrown when validation of {@link com.bz.challenge.service.search.SearchCriterion} fails
 */
public class InvalidQueryException extends RuntimeException {

    public InvalidQueryException(String details) {
        super("Invalid query: " + details);
    }
}
