package com.bz.challenge.service.search.support;

import com.bz.challenge.controller.dto.RecipeSearchDto;
import com.bz.challenge.service.search.SearchCriterion;
import com.bz.challenge.service.search.SearchOperation;
import java.util.Arrays;
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
        var attr = parameter.getParameterAnnotation(SearchQuery.class);
        var value = webRequest.getParameter(attr.value());
        var searchCriteria = Arrays.stream(value.split(",")).map(i -> toSearchCriterion(i.split("!"))).toList();
        return new RecipeSearchDto(searchCriteria);
    }

    private SearchCriterion toSearchCriterion(String[] args) {
        return new SearchCriterion(args[0], SearchOperation.findByValue(args[1]).get(), args[2]); //todo
    }

}

