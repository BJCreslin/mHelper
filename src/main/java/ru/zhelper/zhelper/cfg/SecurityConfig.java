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
import ru.zhelper.zhelper.controllers.ChromeExtensionAuthController;
import ru.zhelper.zhelper.controllers.ChromeExtensionController;
import ru.zhelper.zhelper.models.users.ERole;
import ru.zhelper.zhelper.services.security.JwtConfigurer;
import ru.zhelper.zhelper.services.security.JwtTokenProvider;

@Profile("!test")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String CHROME_API = ChromeExtensionController.URL;
    public static final String CHROME_AUTH = ChromeExtensionAuthController.URL + "/**";
    public static final String CHROME_REGISTRATION = "/chrome_registration/**";

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(CHROME_API).hasAnyRole(ERole.CHROME_EXTENSION.getName(), ERole.ROLE_ADMIN.getName())
                .antMatchers(CHROME_AUTH).permitAll()
                .antMatchers(CHROME_REGISTRATION).permitAll()
                .and().authorizeRequests().antMatchers("/h2-console/**").permitAll().filterSecurityInterceptorOncePerRequest(false)
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().logout().permitAll().and()
                .apply(new JwtConfigurer(jwtTokenProvider));
        http.headers().frameOptions().disable();
    }
}
