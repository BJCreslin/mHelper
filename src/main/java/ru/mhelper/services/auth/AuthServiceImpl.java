package ru.mhelper.services.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mhelper.controllers.exeptions.BadRequestException;
import ru.mhelper.models.dto.*;
import ru.mhelper.models.users.ERole;
import ru.mhelper.models.users.Role;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.RoleRepository;
import ru.mhelper.repository.UserRepository;
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.security.JwtTokenProvider;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    public static final String USERNAME_IS_EXIST = "Error: Username is exist";

    public static final String EMAIL_IS_EXIST = "Error: Email is exist";

    public static final String USER_IS_NOT_FOUND = "Error, Role USER is not found";

    public static final String ADMIN_IS_NOT_FOUND = "Error, Role ADMIN is not found";

    public static final String CHROME_EXTENSION_IS_NOT_FOUND = "Error, Role CHROME EXTENSION is not found";

    public static final String USER_CREATED = "User CREATED";

    public static final String USER_NOT_FOUND = "User with name %s not found";

    private final TelegramCodeService telegramCodeService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<AbstractResponse> getResponseEntity(Integer code) {
        var telegramId = telegramCodeService.getTelegramUserId(code);
        if (!userRepository.existsByTelegramUserId(telegramId)) {
            var newUser = User.createNewTelegramUser(telegramId);
            newUser.setRoles(
                    Set.of(roleRepository.findByName(ERole.CHROME_EXTENSION.getName()).orElseThrow(() -> new BadRequestException(BadRequestException.ROLE_NOT_FOUND)),
                            roleRepository.findByName(ERole.ROLE_TELEGRAM.getName()).orElseThrow(() -> new BadRequestException(BadRequestException.ROLE_NOT_FOUND))));
            userRepository.save(newUser);
        }
        User user = userRepository.findByTelegramUserId(telegramId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList()
                ).build());
    }

    @Override
    public ResponseEntity<JwtResponse> doSignIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(loginRequest.getUserName()).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, loginRequest.getUserName())));
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList()
                ).build());
    }

    @Override
    public ResponseEntity<AbstractResponse> doSignUp(SignUpRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUserName())) {
            LOGGER.error(USERNAME_IS_EXIST);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageResponse.BAD_CODE, USERNAME_IS_EXIST));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            LOGGER.error(EMAIL_IS_EXIST);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageResponse.BAD_CODE, EMAIL_IS_EXIST));
        }

        User user = new User(signupRequest.getUserName(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER.getName())
                    .orElseThrow(() -> new RuntimeException(USER_IS_NOT_FOUND));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin" -> {
                        Role adminRole = roleRepository
                                .findByName(ERole.ROLE_ADMIN.getName())
                                .orElseThrow(() -> new RuntimeException(ADMIN_IS_NOT_FOUND));
                        roles.add(adminRole);
                    }
                    case "chrome" -> {
                        Role modRole = roleRepository
                                .findByName(ERole.CHROME_EXTENSION.getName())
                                .orElseThrow(() -> new RuntimeException(CHROME_EXTENSION_IS_NOT_FOUND));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER.getName())
                                .orElseThrow(() -> new RuntimeException(USER_IS_NOT_FOUND));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(USER_CREATED);
        }
        return ResponseEntity.ok(new MessageResponse(USER_CREATED));
    }
}
