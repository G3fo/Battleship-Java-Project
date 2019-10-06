package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      ScoreRepository scoreRepository,
                                      SalvoRepository salvoRepository) {
        return (args) -> {

            Player player1 = new Player("Jackbauer@gmail.com");
            Player player2 = new Player("Chloe@Brian.com");
            Player player3 = new Player("KimBauer@gmail.com");
            Player player4 = new Player("DavidPalmer@gmail.com");
            Player player5 = new Player("MichelleDessler@gmail.com");

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            playerRepository.save(player5);

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

            gameRepository.saveAll(Arrays.asList(g1, g2, g3, g4, g5, g6, g7, g8));

            GamePlayer gp1 = new GamePlayer(player1, g1);
            GamePlayer gp2 = new GamePlayer(player2, g1);
            GamePlayer gp3 = new GamePlayer(player1, g2);
            GamePlayer gp4 = new GamePlayer(player2, g2);
            GamePlayer gp5 = new GamePlayer(player2, g3);
            GamePlayer gp6 = new GamePlayer(player3, g3);
            GamePlayer gp7 = new GamePlayer(player2, g4);
            GamePlayer gp8 = new GamePlayer(player4, g4);
            GamePlayer gp9 = new GamePlayer(player4, g5);
            GamePlayer gp10 = new GamePlayer(player3, g5);
            GamePlayer gp11 = new GamePlayer(player2, g6);
            GamePlayer gp12 = new GamePlayer(player4, g6);
            GamePlayer gp13 = new GamePlayer(player5, g7);
            GamePlayer gp14 = new GamePlayer(player1, g7);
            GamePlayer gp15 = new GamePlayer(player3, g8);
            GamePlayer gp16 = new GamePlayer(player5, g8);


            gamePlayerRepository.saveAll(Arrays.asList(gp1, gp2, gp3, gp4, gp5, gp6, gp7, gp8, gp9, gp10, gp11, gp12, gp13, gp14, gp15, gp16));


            List<String> locations1 = new ArrayList(Arrays.asList("A2", "A3","A4","A5","A6"));
            List<String> locations2 = new ArrayList(Arrays.asList("F1", "G1", "H1"));
            //List<String> locations3 = new ArrayList(Arrays.asList("C4", "C5"));
            List<String> locations4 = new ArrayList(Arrays.asList("C5", "D5", "E5"));
            List<String> locations5 = new ArrayList(Arrays.asList("G1","G2","G3","G4"));
            List<String> locations6 = new ArrayList(Arrays.asList("D6", "D7"));
            //List<String> locations7 = new ArrayList(Arrays.asList("B2", "B3", "B4"));
            List<String> locations8 = new ArrayList(Arrays.asList("A6","B6"));

            Ship ship1_1_1 = new Ship(ShipType.CARRIER, locations1, gp1);
            Ship ship2_1_1 = new Ship(ShipType.BATTLESHIP, locations2, gp1);
            Ship ship3_1_1 = new Ship(ShipType.CRUISER, locations4, gp2);
            Ship ship1_1_2 = new Ship(ShipType.SUBMARINE, locations5, gp2);
            Ship ship2_1_2 = new Ship(ShipType.DESTROYER, locations6, gp1);

            shipRepository.save(ship2_1_1);
            shipRepository.save(ship3_1_1);
            shipRepository.save(ship1_1_1);
            shipRepository.save(ship1_1_2);
            shipRepository.save(ship2_1_2);

            Salvo salvo1 = new Salvo(1, Arrays.asList("H2","H3","H4","H1"), gp1);
            Salvo salvo2 = new Salvo(1, Arrays.asList("H4","H5","H6"), gp2);
            Salvo salvo3 = new Salvo(2, Arrays.asList("A2","B2","C3"), gp1);
            Salvo salvo4 = new Salvo(3, Arrays.asList("B2","B3","B4"), gp2);
            Salvo salvo5 = new Salvo(4, Arrays.asList("A1","A2","A3"), gp1);

            salvoRepository.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5));

            Score score1_1 = new Score(g1, player1, 1, date1);
            Score score1_2 = new Score(g1, player2, 0, date2);
            Score score2_1 = new Score(g2, player1, 0, date3);
            Score score2_2 = new Score(g2, player2, 0.5F, date4);
            Score score3_1 = new Score(g3, player2, 0.5F, date5);
            Score score3_2 = new Score(g3, player3, 0, date6);
            Score score4_1 = new Score(g4, player1, 1, date7);
            Score score4_2 = new Score(g4, player2, 1, date8);
            Score score5_1 = new Score(g5, player3, 1, date1);
            Score score5_2 = new Score(g5, player1, 0, date2);
            Score score6_1 = new Score(g6, player3, 1, date3);
            Score score6_2 = new Score(g6, player1, 0, date4);
            Score score7_1 = new Score(g7, player3, 0.5F, date5);
            Score score7_2 = new Score(g7, player4, 0, date6);
            Score score8_1 = new Score(g8, player4, 1, date7);

            scoreRepository.save(score1_1);
            scoreRepository.save(score1_2);
            scoreRepository.save(score2_1);
            scoreRepository.save(score2_2);
            scoreRepository.save(score3_1);
            scoreRepository.save(score3_2);
            scoreRepository.save(score4_1);
            scoreRepository.save(score4_2);
            scoreRepository.save(score5_1);
            scoreRepository.save(score5_2);
            scoreRepository.save(score6_1);
            scoreRepository.save(score6_2);
            scoreRepository.save(score7_1);
            scoreRepository.save(score7_2);
            scoreRepository.save(score8_1);


            //FIN MODULO 4
        };
    }
}