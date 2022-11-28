package com.bz.challenge.service.search;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class consists of static utility methods for operating on modifications of {@link com.bz.challenge.service.search.SearchSpecification}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Specifications {

    /**
     * Combines a list of {@link com.bz.challenge.service.search.SearchSpecification} into one via AND operator
     *
     * @param searchSpecificationList list of {@link com.bz.challenge.service.search.SearchSpecification}
     * @return combined specification
     */
    public static <T> Optional<Specification<T>> combineAnd(List<SearchSpecification<T>> searchSpecificationList) {
        return searchSpecificationList.stream()
            .map(searchSpecification -> (Specification<T>) searchSpecification)
            .reduce(Specification::and);
    }

    /**
     * Combines a list of {@link com.bz.challenge.service.search.SearchSpecification} into one via OR operator
     *
     * @param searchSpecificationList list of {@link com.bz.challenge.service.search.SearchSpecification}
     * @return combined specification
     */
    public static <T> Optional<Specification<T>> combineOr(List<SearchSpecification<T>> searchSpecificationList) {
        return searchSpecificationList.stream()
            .map(searchSpecification -> (Specification<T>) searchSpecification)
            .reduce(Specification::and);
    }

}
