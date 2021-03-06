package ru.mhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mhelper.aspect.stop_spam.StopSpam;
import ru.mhelper.cfg.ApiVersion;
import ru.mhelper.controllers.exeptions.BadRequestException;
import ru.mhelper.models.dto.*;
import ru.mhelper.models.jwt.JwtUser;
import ru.mhelper.models.users.ERole;
import ru.mhelper.models.users.Role;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.RoleRepository;
import ru.mhelper.repository.UserRepository;
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.security.JwtTokenProvider;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.mhelper.controllers.AuthController.URL;

@Controller
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/auth";

    public static final String TEST_JWT = "/test";

    public static final String CODE_URL = "/code/";

    public static final String USERNAME_IS_EXIST = "Error: Username is exist";

    public static final String EMAIL_IS_EXIST = "Error: Email is exist";

    public static final String USER_IS_NOT_FOUND = "Error, Role USER is not found";

    public static final String ADMIN_IS_NOT_FOUND = "Error, Role ADMIN is not found";

    public static final String CHROME_EXTENSION_IS_NOT_FOUND = "Error, Role CHROME EXTENSION is not found";

    public static final String AUTHENTICATING = "User with UserName {} is authenticating";

    public static final String CODE_AUTHENTICATING = "User with code {} is authenticating";

    public static final String USER_CREATED = "User CREATED";

    public static final String SUCCESSFUL_CONNECTION = "Successful connection";

    public static final String USER_NOT_FOUND = "User with name %s not found";

    public static final String CODE_NOT_FOUND = "Code not found";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final TelegramCodeService telegramCodeService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRespository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TelegramCodeService telegramCodeService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRespository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.telegramCodeService = telegramCodeService;
    }

    @GetMapping({""})
    @ResponseBody
    public ResponseEntity<MessageResponse> testConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }

    @RolesAllowed({"ADMIN", "CHROME_EXTENSION"})
    @GetMapping({TEST_JWT})
    @ResponseBody
    public ResponseEntity<MessageResponse> testAuthConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }

    @GetMapping({CODE_URL + "{code}"})
    @ResponseBody
    @StopSpam
    public ResponseEntity<AbstractResponse> tgSignIn(@PathVariable Integer code, HttpServletRequest request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(CODE_AUTHENTICATING, code);
        }
        if (telegramCodeService.existByCode(code)) {
            var telegramId = telegramCodeService.getTelegramUserId(code);
            if (!userRepository.existsByTelegramUserId(telegramId)) {
                var newUser = User.createNewTelegramUser(telegramId);
                newUser.setRoles(
                    Set.of(roleRepository.findByName(ERole.CHROME_EXTENSION.getName()).orElseThrow(() -> new BadRequestException(BadRequestException.ROLE_NOT_FOUND)),
                        roleRepository.findByName(ERole.ROLE_TELEGRAM.getName()).orElseThrow(() -> new BadRequestException(BadRequestException.ROLE_NOT_FOUND))));
                userRepository.save(newUser);
            }
            User user = userRepository.findByTelegramUserId(telegramId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));
            String jwt = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList())));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(MessageResponse.BAD_TELEGRAM_CODE, CODE_NOT_FOUND));
        }
    }

    @PostMapping({"/signin"})
    @ResponseBody
    public ResponseEntity<JwtResponse> signIn(@RequestBody LoginRequest loginRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(AUTHENTICATING, loginRequest.getUserName());
        }
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(loginRequest.getUserName()).
            orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, loginRequest.getUserName())));
        JwtUser userDetails = (JwtUser) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        String jwt = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
    }

    @PostMapping(value = {"/signup"}, consumes = {"application/json"})
    public ResponseEntity<AbstractResponse> registerUser(@RequestBody SignUpRequest signupRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Create new User with Name {}, email {}", signupRequest.getUserName(), signupRequest.getEmail());
        }
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
                    case "admin":
                        Role adminRole = roleRepository
                            .findByName(ERole.ROLE_ADMIN.getName())
                            .orElseThrow(() -> new RuntimeException(ADMIN_IS_NOT_FOUND));
                        roles.add(adminRole);

                        break;
                    case "chrome":
                        Role modRole = roleRepository
                            .findByName(ERole.CHROME_EXTENSION.getName())
                            .orElseThrow(() -> new RuntimeException(CHROME_EXTENSION_IS_NOT_FOUND));
                        roles.add(modRole);

                        break;

                    default:
                        Role userRole = roleRepository
                            .findByName(ERole.ROLE_USER.getName())
                            .orElseThrow(() -> new RuntimeException(USER_IS_NOT_FOUND));
                        roles.add(userRole);
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
