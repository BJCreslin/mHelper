package ru.mhelper.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(3)
public class SwaggerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(
                "/swagger-resources/**"
                , "/swagger-ui.html"
                , "/v2/api-docs"
                , "/webjars/**"
                , "/v3/api-docs/**"
                , "/swagger-ui/**"
                , "/swagger-ui/index.html/**");
    }
}
