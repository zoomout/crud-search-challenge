package com.bz.challenge.service.search.support;

import com.bz.challenge.controller.dto.RecipeSearchDto;
import com.bz.challenge.core.exception.types.InvalidQueryException;
import com.bz.challenge.core.exception.types.NotSupportedOperationException;
import com.bz.challenge.service.search.SearchCriterion;
import com.bz.challenge.service.search.SearchOperation;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SearchArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SearchQuery.class) != null;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        final var attr = parameter.getParameterAnnotation(SearchQuery.class);
        final var value = webRequest.getParameter(attr.value());
        final var allowedKeys = attr.allowedKeys();
        if (Strings.isBlank(value)) {
            return new RecipeSearchDto();
        }
        final var searchCriteria = Arrays.stream(value.split(",")).map(args -> toSearchCriterion(args, allowedKeys)).toList();
        return new RecipeSearchDto(searchCriteria);
    }

    private SearchCriterion toSearchCriterion(String query, String[] allowedKeys) {
        String[] args = query.split("!");
        if (args.length != 3) {
            throw new InvalidQueryException("Query parameters size should be equal to 3, query=" + query);
        }
        String key = args[0];
        if (!Arrays.asList(allowedKeys).contains(key)) {
            throw new InvalidQueryException("Key is no allowed: " + key + ". Allowed keys: " + String.join(",", allowedKeys));
        }
        String operation = args[1];
        String value = args[2];
        if (value == null || value.isBlank()) {
            throw new InvalidQueryException("Null and blank value is no allowed: " + value);
        }
        return new SearchCriterion(
            key,
            SearchOperation.findByValue(operation).orElseThrow(() -> new NotSupportedOperationException(operation)),
            value
        );
    }

}

