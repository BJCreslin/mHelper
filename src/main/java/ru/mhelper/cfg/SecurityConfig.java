package ru.mhelper.cfg;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.mhelper.controllers.AuthController;
import ru.mhelper.controllers.ChromeExtensionController;
import ru.mhelper.controllers.SimpleController;
import ru.mhelper.services.security.JwtConfigurer;
import ru.mhelper.services.security.JwtTokenFilter;
import ru.mhelper.services.security.JwtTokenProvider;

import static ru.mhelper.controllers.AuthController.CODE_URL;
import static ru.mhelper.controllers.AuthController.TEST_JWT;

@Profile("!test")
@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String CHROME_API = ChromeExtensionController.URL + "/***";

    public static final String CHROME_AUTH = AuthController.URL + "/";

    public static final String TEST_CHROME_JWT_AUTH = AuthController.URL + TEST_JWT + "/";

    private final JwtConfigurer jwtConfigurer;

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Unauthorized.");
                }))
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Forbidden");
                })).and()
                .csrf().disable()
                .cors()
                .and()
                .httpBasic().disable()
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers("/h2-console**",
                                        CHROME_AUTH,
                                        AuthController.URL + CODE_URL + "***",
                                        SimpleController.INDEX_PAGE_NAME).permitAll().
                                anyRequest().authenticated())
                .anonymous().disable()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
//                .authorizeHttpRequests(auth ->
//                {
//                    try {
//                        auth
//                                .requestMatchers("/h2-console**",
//                                        CHROME_AUTH,
//                                        AuthController.URL + CODE_URL + "***",
//                                        SimpleController.INDEX_PAGE_NAME).permitAll()
//                                .requestMatchers(CHROME_API).hasAnyRole(ERole.CHROME_EXTENSION.getName(), ERole.ROLE_ADMIN.getName())
//                                .requestMatchers(TEST_CHROME_JWT_AUTH).hasAnyRole(ERole.ROLE_ADMIN.getName(), ERole.CHROME_EXTENSION.getName())
//                                .anyRequest().authenticated()
//                                .and().formLogin().permitAll()
//                                .and().logout().permitAll().and()
//                                .apply(jwtConfigurer);
//                    } catch (Exception e) {
//                        throw new SecurityException(e);
//                    }
//                }).build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
