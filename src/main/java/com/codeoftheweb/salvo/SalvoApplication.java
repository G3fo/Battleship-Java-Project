package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

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
			LocalDateTime date1 = LocalDateTime.now().plusHours(0);
			LocalDateTime date2 = LocalDateTime.now().plusHours(1);
			LocalDateTime date3 = LocalDateTime.now().plusHours(2);

			GameRepository.save(new Game(date1));
			GameRepository.save(new Game(date2));
			GameRepository.save(new Game(date3));
		};
	}

}