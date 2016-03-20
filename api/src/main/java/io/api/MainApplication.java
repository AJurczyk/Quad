package io.api;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource({"classpath:springConfig.xml"})
@SuppressWarnings("PMD.UseUtilityClass")
public class MainApplication {
    public static void main(String... args) {
        SpringApplication.run(MainApplication.class, args);
    }

}