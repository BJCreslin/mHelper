package ru.mhelper.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.mhelper.models.BaseStatus;
import ru.mhelper.models.users.ERole;
import ru.mhelper.models.users.Role;
import ru.mhelper.repository.RoleRepository;

import java.util.Arrays;

@Component
@Order(2)
@Profile("dev")
public class RoleInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleInitializer.class);
    private final RoleRepository repository;

    public RoleInitializer(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Arrays.stream(ERole.values()).forEach(x -> {
            if (repository.findByName(x.getName()).isEmpty()) {
                var role = new Role(x.getName());
                role.setStatus(BaseStatus.ACTIVE);
                repository.save(role);
            }
        });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Roles were initiated");
        }
    }
}
