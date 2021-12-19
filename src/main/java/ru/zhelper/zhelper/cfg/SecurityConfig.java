package ru.zhelper.zhelper.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.zhelper.zhelper.controllers.AuthController;
import ru.zhelper.zhelper.controllers.ChromeExtensionController;
import ru.zhelper.zhelper.models.users.ERole;
import ru.zhelper.zhelper.services.security.JwtConfigurer;

import static ru.zhelper.zhelper.controllers.AuthController.TEST_JWT;

@Profile("!test")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,
        securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String CHROME_API = ChromeExtensionController.URL + "/***";
    public static final String CHROME_AUTH = AuthController.URL + "/";
    public static final String TEST_CHROME_JWT_AUTH = AuthController.URL + TEST_JWT + "/";
    public static final String CHROME_REGISTRATION = CHROME_AUTH + "code/***";

    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();
        http.headers().frameOptions().disable();
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        http.authorizeRequests()
                .antMatchers(CHROME_API).hasAnyRole(ERole.CHROME_EXTENSION.getName(), ERole.ROLE_ADMIN.getName())
                .antMatchers(TEST_CHROME_JWT_AUTH).hasAnyRole(ERole.ROLE_ADMIN.getName(), ERole.CHROME_EXTENSION.getName())
                .antMatchers(CHROME_REGISTRATION).permitAll()
                .antMatchers(CHROME_AUTH).permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/h2-console/**").permitAll().filterSecurityInterceptorOncePerRequest(false)
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().logout().permitAll().and()
                .apply(jwtConfigurer);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
