package ru.mhelper.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.mhelper.exceptions.DaoException;
import ru.mhelper.models.users.ERole;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.RoleRepository;
import ru.mhelper.repository.UserRepository;

import java.util.ArrayList;
import java.util.Set;

import static ru.mhelper.exceptions.DaoException.ERROR_GET_ROLE;

@Component
@Order(3)
@Profile("!ci")
public class FakeUserDataInitializer implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeUserDataInitializer.class);
    private final boolean enabled;

    public static final int CHROME_USERS = 3;

    public static final String ADMIN_NAME = "Admin";
    public static final String ADMIN_EMAIL = "admin@zhelper.ru";
    public static final String ADMIN_PASSWORD = "$2a$12$LfwRzy/Qan/QDEoA3.LbHe1bvU7ZoZQpykuPF7P2EGe/dAkn4Td3C";// password
    public static final Long ADMIN_TELEGRAM = 800000L;

    public static final String USER_NAME = "User%d";
    public static final String USER_EMAIL = "user%d@zhelper.ru";
    public static final String USER_PASSWORD = "$$2a$12$tFQ3a5wnxoSX9p90FtDxse6BUuHoT/v1QumMdAtSnxTnEdpASNYkW";// password1

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
            users.add(User.builder()
                    .username(String.format(USER_NAME, i))
                    .email(String.format(USER_EMAIL, i))
                    .password(USER_PASSWORD)
                    .enabled(true)
                    .telegramUserId(ADMIN_TELEGRAM + i)
                    .roles(Set.of(role)).build()
            );
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
                .telegramUserId(ADMIN_TELEGRAM-100)
                .roles(Set.of(role)).build();
        userRepository.save(user);
    }
}
