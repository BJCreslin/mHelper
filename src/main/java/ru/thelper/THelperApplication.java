package ru.thelper;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class THelperApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(THelperApplication.class, args);
    }

}
