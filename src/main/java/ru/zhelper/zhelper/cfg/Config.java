package ru.zhelper.zhelper.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import ru.zhelper.zhelper.exceptions.DaoException;
import ru.zhelper.zhelper.models.BaseStatus;
import ru.zhelper.zhelper.models.users.ERole;
import ru.zhelper.zhelper.models.users.Role;
import ru.zhelper.zhelper.repository.RoleRepository;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
//@EnableTransactionManagement
//@Profile("ci")
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final String ENV_JDBC = "java:comp/env/jdbc/zhelperdb";
    private static final String NAMING_EXCEPTION_FOR = "NamingException for ";

    // @Bean
    DataSource dataSource() {
        DataSource dataSource = null;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup(ENV_JDBC, DataSource.class);
        } catch (NamingException e) {
            String message = NAMING_EXCEPTION_FOR + ENV_JDBC;
            LOGGER.error(message, e);
            throw new DaoException(message, e);
        }
        return dataSource;
    }

    private final RoleRepository repository;

    public Config(RoleRepository roleRepository) {
        this.repository = roleRepository;
    }

    @PostConstruct
    public void populateRoleTable() {
        Arrays.stream(ERole.values()).forEach(x -> {
            if (repository.findByName(x.getName()).isEmpty()) {
                var role = new Role(x.getName());
                role.setStatus(BaseStatus.ACTIVE);
                repository.saveAndFlush(role);
            }
        });
    }
}
