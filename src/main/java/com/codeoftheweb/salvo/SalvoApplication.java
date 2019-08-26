package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository PlayerRepository,
                                      GameRepository GameRepository,
                                      GamePlayerRepository gamePlayerRepository) {
        return (args) -> {

            Player player1 = new Player("Jackbauer@gmail.com");
            Player player2 = new Player("Chloe@Brian.com");
            Player player3 = new Player("KimBauer@gmail.com");
            Player player4 = new Player("DavidPalmer@gmail.com");
            Player player5 = new Player("MichelleDessler@gmail.com");

            PlayerRepository.save(player1);
            PlayerRepository.save(player2);
            PlayerRepository.save(player3);
            PlayerRepository.save(player4);
            PlayerRepository.save(player5);

            Date date1 = new Date();
            Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
            Date date3 = Date.from(date2.toInstant().plusSeconds(3600));

            Game g1 = new Game(date1);
            Game g2 = new Game(date2);
            Game g3 = new Game(date3);

            GameRepository.save(g1);
            GameRepository.save(g2);
            GameRepository.save(g3);

            GamePlayer gp1 = new GamePlayer(date1, player1, g1);
            GamePlayer gp2 = new GamePlayer(date1, player2, g1);
            GamePlayer gp3 = new GamePlayer(date2, player3, g2);
            GamePlayer gp4 = new GamePlayer(date2, player4, g2);
            GamePlayer gp5 = new GamePlayer(date3, player5, g3);
            GamePlayer gp6 = new GamePlayer(date3, player1, g3);

            gamePlayerRepository.save(gp1);
            gamePlayerRepository.save(gp2);
            gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);
            gamePlayerRepository.save(gp5);
            gamePlayerRepository.save(gp6);

        };
    }

}