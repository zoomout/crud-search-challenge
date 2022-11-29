package com.bz.challenge.core.configuration;

import com.bz.challenge.service.search.support.SearchArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration to add a custom argument resolver for {@link org.springframework.web.bind.annotation.RestController}
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SearchArgumentResolver());
    }

}
