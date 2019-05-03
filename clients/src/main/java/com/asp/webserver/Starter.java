
package com.asp.webserver;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * Our Spring Boot application.
 */

@SpringBootApplication
public class Starter {
	/**
	 * Starts our Spring Boot application.
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Starter.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setWebEnvironment(true);
		app.run(args);
	}
}
