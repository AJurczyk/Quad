package io.api;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:configSimulatorLocal.xml"})
//@ImportResource({"classpath:configReal.xml"})
@SuppressWarnings("PMD.UseUtilityClass")
public class MainApplication {
    public static void main(String... args) {
        try {
            SpringApplication.run(MainApplication.class, args);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}