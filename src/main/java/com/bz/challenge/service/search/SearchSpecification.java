package com.bz.challenge.service.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.hibernate.query.sqm.tree.domain.SqmPluralValuedSimplePath;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification implementation aware of search operations. Implements search operations defined in {@link
 * com.bz.challenge.service.search.SearchOperation}
 *
 * @param <T> data type
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchSpecification<T> implements Specification<T> {

    private final SearchCriterion search;

    public static <T> SearchSpecification<T> with(SearchCriterion search) {
        return new SearchSpecification<>(search);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final var value = search.getValue();
        String[] split = search.getKey().split("\\."); //todo move higher and validate
        Path<Object> objectPath = root.get(split[0]);

        Class<Object> clazz;
        if (objectPath instanceof SqmBasicValuedSimplePath) {
            clazz = ((SqmBasicValuedSimplePath<Object>) objectPath).getBindableJavaType();
        } else if (objectPath instanceof SqmPluralValuedSimplePath) {
            return switch (search.getOperation()) {
                case CONTAINS -> cb.equal(root.get(split[0]).get(split[1]), value);
                case DOES_NOT_CONTAIN -> cb.notEqual(root.get(split[0]).get(split[1]), value);
                default -> throw new IllegalArgumentException("Invalid search operation '" + search.getOperation() + "' for join");
            };
        } else {
            throw new IllegalArgumentException("Not supported operation '" + search.getOperation() + "' on " + objectPath);
        }

        if (clazz.equals(String.class)) {
            final var expression = cb.lower(root.get(search.getKey()));
            return getStringPredicate(cb, value.toLowerCase(), expression);
        }
        if (clazz.equals(Boolean.class)) {
            Path<Boolean> expression = root.get(search.getKey());
            return getBooleanPredicate(cb, Boolean.valueOf(value), expression);
        }
        if (clazz.equals(Integer.class)) {
            Path<Integer> expression = root.get(search.getKey());
            return getIntegerPredicate(cb, Integer.valueOf(value), expression);
        }
        throw new IllegalArgumentException("Not supported query type: " + clazz.getSimpleName());
    }

    private Predicate getStringPredicate(CriteriaBuilder cb, String value, Expression<String> expression) {
        return switch (search.getOperation()) {
            case CONTAINS -> cb.like(expression, "%" + value + "%");
            case DOES_NOT_CONTAIN -> cb.notLike(expression, "%" + value + "%");
            case EQUAL -> cb.equal(expression, value);
            case NOT_EQUAL -> cb.notEqual(expression, value);
        };
    }

    private Predicate getBooleanPredicate(CriteriaBuilder cb, Boolean value, Path<Boolean> path) {
        return switch (search.getOperation()) {
            case EQUAL -> cb.equal(path, value);
            case NOT_EQUAL -> cb.notEqual(path, value);
            default -> throw new IllegalArgumentException("Invalid search operation '" + search.getOperation() + "' for boolean");
        };
    }

    private Predicate getIntegerPredicate(CriteriaBuilder cb, Integer value, Path<Integer> path) {
        return switch (search.getOperation()) {
            case EQUAL -> cb.equal(path, value);
            case NOT_EQUAL -> cb.notEqual(path, value);
            default -> throw new IllegalArgumentException("Invalid search operation '" + search.getOperation() + "' for boolean");
        };
    }

}
