package ru.zhelper.zhelper.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.zhelper.zhelper.models.users.ERole;
import ru.zhelper.zhelper.models.users.Role;
import ru.zhelper.zhelper.models.users.User;
import ru.zhelper.zhelper.repository.RoleRepository;
import ru.zhelper.zhelper.repository.UserRepository;

import java.util.ArrayList;
import java.util.Set;

@Component
@Order(2)
public class FakeUserDataInitializer implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeUserDataInitializer.class);
    private final boolean enabled;

    public static final int CHROME_USERS = 3;

    public static final String ADMIN_NAME = "Admin";
    public static final String ADMIN_EMAIL = "admin@zhelper.ru";
    public static final String ADMIN_PASSWORD = "$2a$12$LfwRzy/Qan/QDEoA3.LbHe1bvU7ZoZQpykuPF7P2EGe/dAkn4Td3C";// password
    public static final String ADMIN_TELEGRAM = "ADMIN_TELEGRAM";

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
        var role = new Role(ERole.CHROME_EXTENSION.getName());
        roleRepository.save(role);
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
        var role = new Role(ERole.ROLE_ADMIN.getName());
        roleRepository.save(role);
        User user = User.builder()
                .username(ADMIN_NAME)
                .email(ADMIN_EMAIL)
                .password(ADMIN_PASSWORD)
                .enabled(true)
                .telegramUserId(ADMIN_TELEGRAM)
                .roles(Set.of(role)).build();
        userRepository.save(user);
    }
}
