package com.bz.challenge.service.search.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to parse a search query sent via Rest API
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface SearchQuery {

    String value();

    String[] allowedKeys();

    int maxGroups() default 10;

}
