package com.bz.challenge.service.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.tree.domain.SqmBasicValuedSimplePath;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification implementation aware of search operations. Implements search operations defined in {@link
 * com.bz.challenge.service.search.SearchOperation}
 *
 * @param <T> data type
 */
@RequiredArgsConstructor
public class SearchSpecification<T> implements Specification<T> {

    private final SearchCriterion searchCriterion;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        var value = searchCriterion.getValue();
        Path<Object> objectPath = root.get(searchCriterion.getKey());

        Class<Object> clazz = ((SqmBasicValuedSimplePath<Object>) objectPath).getBindableJavaType();

        if (clazz.equals(String.class)) {
            var expression = criteriaBuilder.lower(root.get(searchCriterion.getKey()));
            return getStringPredicate(criteriaBuilder, value.toLowerCase(), expression);
        }
        if (clazz.equals(Boolean.class)) {
            Path<Boolean> expression = root.get(searchCriterion.getKey());
            return getBooleanPredicate(criteriaBuilder, Boolean.valueOf(value), expression);
        }
        if (clazz.equals(Integer.class)) {
            Path<Integer> expression = root.get(searchCriterion.getKey());
            return getIntegerPredicate(criteriaBuilder, Integer.valueOf(value), expression);
        }
        throw new IllegalArgumentException("Not supported query type: " + clazz.getSimpleName());
    }

    private Predicate getStringPredicate(CriteriaBuilder criteriaBuilder, String value, Expression<String> expression) {
        return switch (searchCriterion.getOperation()) {
            case CONTAINS -> criteriaBuilder.like(expression, "%" + value + "%");
            case DOES_NOT_CONTAIN -> criteriaBuilder.notLike(expression, "%" + value + "%");
            case EQUAL -> criteriaBuilder.equal(expression, value);
            case NOT_EQUAL -> criteriaBuilder.notEqual(expression, value);
        };
    }

    private Predicate getBooleanPredicate(CriteriaBuilder criteriaBuilder, Boolean value, Path<Boolean> path) {
        return switch (searchCriterion.getOperation()) {
            case EQUAL -> criteriaBuilder.equal(path, value);
            case NOT_EQUAL -> criteriaBuilder.notEqual(path, value);
            default -> throw new IllegalArgumentException("Invalid search operation '" + searchCriterion.getOperation() + "' for boolean");
        };
    }

    private Predicate getIntegerPredicate(CriteriaBuilder criteriaBuilder, Integer value, Path<Integer> path) {
        return switch (searchCriterion.getOperation()) {
            case EQUAL -> criteriaBuilder.equal(path, value);
            case NOT_EQUAL -> criteriaBuilder.notEqual(path, value);
            default -> throw new IllegalArgumentException("Invalid search operation '" + searchCriterion.getOperation() + "' for boolean");
        };
    }

}
