package com.bz.challenge.core.configuration;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Configures specific behavior of Spring Data REST
 */
@Component
public class DataRestConfigurer implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration restConfiguration, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(restConfiguration, cors);
        restConfiguration.getExposureConfiguration()
            .disablePutForCreation()
            .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(HttpMethod.PATCH)))
            .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(HttpMethod.PATCH));
    }

}
