package ru.mhelper.cfg;

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
import ru.mhelper.controllers.AuthController;
import ru.mhelper.controllers.ChromeExtensionController;
import ru.mhelper.controllers.SimpleController;
import ru.mhelper.models.users.ERole;
import ru.mhelper.services.security.JwtConfigurer;

import static ru.mhelper.controllers.AuthController.TEST_JWT;

@Profile("!test")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String CHROME_API = ChromeExtensionController.URL + "/***";

    public static final String CHROME_AUTH = AuthController.URL + "/";

    public static final String TEST_CHROME_JWT_AUTH = AuthController.URL + TEST_JWT + "/";

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

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        http.authorizeRequests()
            .antMatchers(CHROME_API).hasAnyRole(ERole.CHROME_EXTENSION.getName(), ERole.ROLE_ADMIN.getName())
            .antMatchers(TEST_CHROME_JWT_AUTH).hasAnyRole(ERole.ROLE_ADMIN.getName(), ERole.CHROME_EXTENSION.getName())
            .antMatchers(CHROME_AUTH).permitAll()
            .antMatchers("/h2-console/**").permitAll().filterSecurityInterceptorOncePerRequest(false)
            .antMatchers(SimpleController.INDEX_PAGE_NAME).permitAll()
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
