package com.oldfield.lnrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LnrsSpringExerciseApplication {

	public static final String API_KEY_HEADER = "x-api-key";

	public static void main(String[] args) {
		SpringApplication.run(LnrsSpringExerciseApplication.class, args);
	}

}
