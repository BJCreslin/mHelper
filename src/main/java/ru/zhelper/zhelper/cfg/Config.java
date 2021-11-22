package ru.zhelper.zhelper.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jndi.JndiTemplate;
import ru.zhelper.zhelper.exceptions.DaoException;

import javax.naming.NamingException;
import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement
//@Profile("ci")
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final String ENV_JDBC = "java:comp/env/jdbc/zhelperdb";
    private static final String NAMING_EXCEPTION_FOR = "NamingException for ";

    @Bean
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
}
