package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

  @Autowired
  private GameRepository gameRepository;
  @Autowired
  private GamePlayerRepository gamePlayerRepository;
  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  @RequestMapping("/leaderboard")
  public List<Object> getLeaderboard() {
    return playerRepository
            .findAll()
            .stream()
            .map(player -> player.toGameHistory())
            .collect(Collectors.toList());
  }

  //public Map<String, Object> getAllGames() {
  //    Map<String, Object> dto = new HashMap<>();
  //    dto.put("games", gameRepository
  //           .findAll()
  //           .stream()
  //            .map(Game::createGameDTO)
  //            .collect(toList()));
  //    return dto;
  //}

  @RequestMapping("/games")
  public Map<String, Object> getAllGames(Authentication authentication) {
    Map<String, Object> dto = new HashMap<>();
    if (!this.isGuest(authentication))
      dto.put("player", playerRepository.findByUserName(authentication.getName()).createGameDTO_Player());
    else
      dto.put("player", "Guest");
    dto.put("games", gameRepository
            .findAll()
            .stream()
            .map(Game::createGameDTO)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean isGuest(Authentication authentication) {
    return authentication == null || authentication instanceof AnonymousAuthenticationToken;
  }

  @RequestMapping(path = "/players", method = RequestMethod.POST)
  public ResponseEntity<String> createUser(@RequestParam String username,
                                                        @RequestParam String password) {

    if (username.isEmpty()) {
      return new ResponseEntity<>("No username given",HttpStatus.FORBIDDEN);
    }
    Player player = playerRepository.findByUserName(username);
    if (player != null) {
      return new ResponseEntity<>("Username already in use",HttpStatus.CONFLICT);
    }
    Player newPlayer = playerRepository.save(new Player(username, passwordEncoder.encode(password)));

    return new ResponseEntity<>(newPlayer.getUserName(), HttpStatus.CREATED);
  }

//  @RequestMapping(path = "/games")

  @RequestMapping("/game_view/{gamePlayerId}")
  public Map<String, Object> getGameView(@PathVariable Long gamePlayerId) {

    GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

    Map<String, Object> dto = gamePlayer.getGame().createGameDTO();
    dto.put("ships", gamePlayer.getShips().stream().map(Ship::createGameDTO_Ship));
    dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream()
            .flatMap(gp -> gp.getSalvoes().stream().map(Salvo::createGameDTO_Salvo))
            .collect(Collectors.toList()));
    return dto;
  }
}