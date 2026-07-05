package com.bakersin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Extending SpringBootServletInitializer is what lets this app boot correctly when its WAR
 * is deployed into an external servlet container (Tomcat 9) rather than only via
 * `java -jar` / the embedded container. main() is still kept for local development.
 */
@SpringBootApplication
public class BakersInApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BakersInApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BakersInApplication.class);
    }
}
