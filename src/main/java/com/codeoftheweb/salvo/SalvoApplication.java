package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Player("Jackbauer@gmail.com"));
			repository.save(new Player("Chloe@Brian.com"));
			repository.save(new Player("KimBauer@gmail.com"));
			repository.save(new Player("DavidPalmer@gmail.com"));
			repository.save(new Player("MichelleDessler@gmail.com"));
		};
	}
}