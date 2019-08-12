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
	public CommandLineRunner initData(PlayerRepository PlayerRepository,GameRepository GameRepository) {
		return (args) -> {
			// Save a couple of customers
			PlayerRepository.save(new Player("Jackbauer@gmail.com"));
			PlayerRepository.save(new Player("Chloe@Brian.com"));
			PlayerRepository.save(new Player("KimBauer@gmail.com"));
			PlayerRepository.save(new Player("DavidPalmer@gmail.com"));
			PlayerRepository.save(new Player("MichelleDessler@gmail.com"));

			// Saves a couple of games
			GameRepository.save(new Game());
			GameRepository.save(new Game());
			GameRepository.save(new Game());
		};
	}

}