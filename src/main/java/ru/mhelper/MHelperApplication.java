package ru.mhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MHelperApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MHelperApplication.class, args);
    }

}
