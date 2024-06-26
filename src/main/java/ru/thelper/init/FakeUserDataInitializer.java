package ru.thelper.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.thelper.exceptions.DaoException;
import ru.thelper.models.BaseStatus;
import ru.thelper.models.users.ERole;
import ru.thelper.models.users.TelegramStateType;
import ru.thelper.models.users.User;
import ru.thelper.repository.RoleRepository;
import ru.thelper.repository.UserRepository;

import java.util.ArrayList;
import java.util.Set;

import static ru.thelper.exceptions.DaoException.ERROR_GET_ROLE;

@Component
@Order(3)
@Profile("dev")
public class FakeUserDataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(FakeUserDataInitializer.class);

    private final boolean enabled;

    public static final int CHROME_USERS = 3;

    public static final String ADMIN_NAME = "Admin";

    public static final String ADMIN_EMAIL = "admin@mhelper.ru";

    public static final String ADMIN_PASSWORD = "$2a$12$LfwRzy/Qan/QDEoA3.LbHe1bvU7ZoZQpykuPF7P2EGe/dAkn4Td3C";// password

    public static final Long ADMIN_TELEGRAM = 800000L;

    public static final String USER_NAME = "User%d";

    public static final String USER_EMAIL = "user%d@mhelper.ru";

    public static final String USER_PASSWORD = "$2a$12$tFQ3a5wnxoSX9p90FtDxse6BUuHoT/v1QumMdAtSnxTnEdpASNYkW";// password1

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public FakeUserDataInitializer(@Value("true") boolean enabled, UserRepository userRepository, RoleRepository roleRepository) {
        this.enabled = enabled;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled) {
            return;
        }
        createAdmin();
        createUsers();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Dummy users were created");
        }
    }

    private void createUsers() {
        var users = new ArrayList<User>();
        var role = roleRepository.findByName(ERole.CHROME_EXTENSION.getName()).orElseThrow(() -> new DaoException(ERROR_GET_ROLE));
        for (int i = 0; i < CHROME_USERS; i++) {
            User user = User.builder()
                .username(String.format(USER_NAME, i))
                .email(String.format(USER_EMAIL, i))
                .password(USER_PASSWORD)
                .enabled(true)
                .telegramUserId(ADMIN_TELEGRAM + i)
                .comment("User")
                .telegramStateType(TelegramStateType.NO_STATEMENT)
                .roles(Set.of(role)).build();
            user.setStatus(BaseStatus.ACTIVE);
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    private void createAdmin() {
        var role = roleRepository.findByName(ERole.ROLE_ADMIN.getName()).orElseThrow(() -> new DaoException(ERROR_GET_ROLE));
        User user = User.builder()
            .username(ADMIN_NAME)
            .email(ADMIN_EMAIL)
            .password(ADMIN_PASSWORD)
            .enabled(true)
            .comment(ADMIN_NAME)
            .telegramStateType(TelegramStateType.NO_STATEMENT)
            .telegramUserId(ADMIN_TELEGRAM - 100)
            .roles(Set.of(role)).build();
        user.setStatus(BaseStatus.ACTIVE);
        userRepository.save(user);
    }
}
