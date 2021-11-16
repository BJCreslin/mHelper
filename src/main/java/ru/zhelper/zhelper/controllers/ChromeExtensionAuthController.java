package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.zhelper.zhelper.cfg.jwt.JwtUtils;
import ru.zhelper.zhelper.models.dto.JwtResponse;
import ru.zhelper.zhelper.models.dto.LoginRequest;
import ru.zhelper.zhelper.services.security.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"chrome_auth"})
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChromeExtensionAuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChromeExtensionAuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ChromeExtensionAuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping({"", "/"})
    @ResponseBody
    public String testAuth() {
        return "hello";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("User with UserName {} is authenticating", loginRequest.getUserName());
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}
