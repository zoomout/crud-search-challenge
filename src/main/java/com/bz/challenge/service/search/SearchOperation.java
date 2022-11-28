package com.bz.challenge.service.search;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Search operation to be used in {@link com.bz.challenge.service.search.SearchSpecification}
 */
@Getter
@RequiredArgsConstructor
public enum SearchOperation {

    CONTAINS("cn"),
    DOES_NOT_CONTAIN("nc"),
    EQUAL("eq"),
    NOT_EQUAL("ne");

    private final String value;

    public static Optional<SearchOperation> findByValue(String value) {
        return Arrays.stream(SearchOperation.values()).filter(v -> Objects.equals(value, v.getValue())).findAny();
    }

}
