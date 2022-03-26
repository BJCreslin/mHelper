package ru.mhelper.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.mhelper.controllers.AuthController;

import static ru.mhelper.controllers.AuthController.CODE_URL;

@Profile("!test")
@Configuration
@Order(4)
public class TgSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(AuthController.URL + CODE_URL + "***");
    }
}
