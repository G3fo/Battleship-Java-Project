package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      ScoreRepository scoreRepository,
                                      SalvoRepository salvoRepository) {
        return (args) -> {

            Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
            Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
            Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
            Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder.encode("mole"));


            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);

            Date date8 = new Date();
            Date date7 = Date.from(date8.toInstant().minusSeconds(3600));
            Date date6 = Date.from(date7.toInstant().minusSeconds(3600));
            Date date5 = Date.from(date6.toInstant().minusSeconds(3600));
            Date date4 = Date.from(date5.toInstant().minusSeconds(3600));
            Date date3 = Date.from(date4.toInstant().minusSeconds(3600));
            Date date2 = Date.from(date3.toInstant().minusSeconds(3600));
            Date date1 = Date.from(date2.toInstant().minusSeconds(3600));

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
            GamePlayer gp13 = new GamePlayer(player1, g7);
            GamePlayer gp14 = new GamePlayer(player3, g8);


            gamePlayerRepository.saveAll(Arrays.asList(gp1, gp2, gp3, gp4, gp5, gp6, gp7, gp8, gp9, gp10, gp11, gp12, gp13, gp14));


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

@EnableWebSecurity
@Configuration
class WebSecurityAuthorization extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/games.html", "/api/games", "/api/login", "/api/players", "/api/leaderboard").permitAll()
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/api/**", "/web/game.html**").hasAuthority("USER")
                .anyRequest().denyAll();
        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout");
        // turn off checking for CSRF tokens
        http.csrf().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

@Configuration
class WebSecurityAuthentication extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepo;
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepo.findByUserName(inputName);
            if (player != null) {
                return new User(player.getUserName(),player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}