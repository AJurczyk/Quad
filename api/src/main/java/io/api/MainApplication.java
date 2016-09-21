package io.api;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.Locale;


@SpringBootApplication
@ImportResource({"classpath:configReal.xml"})
@SuppressWarnings("PMD.UseUtilityClass")
public class MainApplication {
    public static void main(String... args) {
        SpringApplication.run(MainApplication.class, args);
    }

}