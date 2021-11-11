package ru.zhelper.zhelper.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.zhelper.zhelper.models.users.ERole;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().
                and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/chrome")
                .hasRole(ERole.ROLE_CHROME_EXTENSION.getName())
                .anyRequest()
                .denyAll()
                .and()
                .oauth2ResourceServer()
                .jwt();
    }
}
