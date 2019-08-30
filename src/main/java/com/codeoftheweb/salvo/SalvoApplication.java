package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

            //List<String> locations1 = new ArrayList(Arrays.asList("F6","G6"));
            //List<String> locations2 = new ArrayList(Arrays.asList("E1", "F1", "G1"));
            //List<String> locations3 = new ArrayList(Arrays.asList("B4", "B5"));
            //List<String> locations4 = new ArrayList(Arrays.asList("B5", "C5", "D5"));
            //List<String> locations5 = new ArrayList(Arrays.asList("F1","F2","F3","F4"));
            //List<String> locations6 = new ArrayList(Arrays.asList("B6", "B7"));
            //List<String> locations7 = new ArrayList(Arrays.asList("A2", "A3", "A4"));
            //List<String> locations8 = new ArrayList(Arrays.asList("H2", "H3","H4","H5","H6"));


            //Ship ship1_1_1 = new Ship(ShipType.CARRIER, locations1);
            //Ship ship2_1_1 = new Ship(ShipType.BATTLESHIP, locations2);
            //Ship ship3_1_1 = new Ship(ShipType.CRUISER, locations4);
            //Ship ship1_1_2 = new Ship(ShipType.SUBMARINE, locations5);
            //Ship ship2_1_2 = new Ship(ShipType.DESTROYER, locations8);



            PlayerRepository.save(player1);
            PlayerRepository.save(player2);
            PlayerRepository.save(player3);
            PlayerRepository.save(player4);
            PlayerRepository.save(player5);

            Date date1 = new Date();
            Date date2 = Date.from(date1.toInstant().plusSeconds(3600));
            Date date3 = Date.from(date2.toInstant().plusSeconds(3600));
            Date date4 = Date.from(date3.toInstant().plusSeconds(3600));
            Date date5 = Date.from(date4.toInstant().plusSeconds(3600));
            Date date6 = Date.from(date5.toInstant().plusSeconds(3600));
            Date date7 = Date.from(date6.toInstant().plusSeconds(3600));
            Date date8 = Date.from(date7.toInstant().plusSeconds(3600));

            Game g1 = new Game(date1);
            Game g2 = new Game(date2);
            Game g3 = new Game(date3);
            Game g4 = new Game(date4);
            Game g5 = new Game(date5);
            Game g6 = new Game(date6);
            Game g7 = new Game(date7);
            Game g8 = new Game(date8);

            GameRepository.save(g1);
            GameRepository.save(g2);
            GameRepository.save(g3);
            GameRepository.save(g4);
            GameRepository.save(g5);
            GameRepository.save(g6);
            GameRepository.save(g7);
            GameRepository.save(g8);

            GamePlayer gp1 = new GamePlayer(date1, player1, g1);
            GamePlayer gp2 = new GamePlayer(date1, player2, g1);
            GamePlayer gp3 = new GamePlayer(date2, player1, g2);
            GamePlayer gp4 = new GamePlayer(date2, player2, g2);
            GamePlayer gp5 = new GamePlayer(date3, player2, g3);
            GamePlayer gp6 = new GamePlayer(date3, player3, g3);
            GamePlayer gp7 = new GamePlayer(date4, player2, g4);
            GamePlayer gp8 = new GamePlayer(date4, player4, g4);
            GamePlayer gp9 = new GamePlayer(date5, player4, g5);
            GamePlayer gp10 = new GamePlayer(date5, player3, g5);
            GamePlayer gp11 = new GamePlayer(date6, player2, g6);
            GamePlayer gp12 = new GamePlayer(date6, player4, g6);
            GamePlayer gp13 = new GamePlayer(date7, player5, g7);
            GamePlayer gp14 = new GamePlayer(date7, player1, g7);
            GamePlayer gp15 = new GamePlayer(date8, player3, g8);
            GamePlayer gp16 = new GamePlayer(date8, player5, g8);


            gamePlayerRepository.save(gp1);
            gamePlayerRepository.save(gp2);
            gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);
            gamePlayerRepository.save(gp5);
            gamePlayerRepository.save(gp6);
            gamePlayerRepository.save(gp7);
            gamePlayerRepository.save(gp8);
            gamePlayerRepository.save(gp9);
            gamePlayerRepository.save(gp10);
            gamePlayerRepository.save(gp11);
            gamePlayerRepository.save(gp12);
            gamePlayerRepository.save(gp13);
            gamePlayerRepository.save(gp14);
            gamePlayerRepository.save(gp15);
            gamePlayerRepository.save(gp16);

        };
    }

}