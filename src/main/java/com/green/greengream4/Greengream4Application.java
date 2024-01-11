package com.green.greengream4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class Greengream4Application {

    public static void main(String[] args) {
        SpringApplication.run(Greengream4Application.class, args);
    }

}
