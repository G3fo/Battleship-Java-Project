package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.repos.GamePlayerRepository;
import com.codeoftheweb.salvo.repos.GameRepository;
import com.codeoftheweb.salvo.repos.PlayerRepository;
import com.codeoftheweb.salvo.repos.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
  private ShipRepository shipRepo;
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

  @RequestMapping(path = "/games", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<Object> createGame(Authentication authentication) {
    ResponseEntity<Object> response;
    if (!authentication.isAuthenticated()) {
      response = new ResponseEntity<>(makeMap("error", "Not authenticated"), HttpStatus.FORBIDDEN);
    }
    Player player = playerRepository.findByUserName(authentication.getName());
    if (player != null) {
        Game newGame = gameRepository.save(new Game(new Date()));
        GamePlayer newGamePlayer;
        newGamePlayer =  new GamePlayer(new Date(), player, newGame);
        gamePlayerRepository.save(newGamePlayer);
        response = new ResponseEntity<>(makeMap("gpid", newGamePlayer.getGamePlayerId()), HttpStatus.CREATED);
      }
    else{
      response = new ResponseEntity<>(makeMap("error", "No User Found"), HttpStatus.FORBIDDEN);
    }
    return response;
  }


  @RequestMapping(path = "/game/{game_id}/players", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<Object> joinGame(@PathVariable Long game_id, Authentication authentication) {
    ResponseEntity<Object> response;
    if (isGuest(authentication)) {
      response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    else {
      Optional<Game> OptionalGame = gameRepository.findById(game_id);
      Game game = new Game();
      if (OptionalGame.isPresent()) {
        game = OptionalGame.get();
        if (game.getGamePlayers().stream().count() > 1) {
          response = new ResponseEntity<>("The game is full", HttpStatus.FORBIDDEN);
        }
        else {
          Player player = playerRepository.findByUserName(authentication.getName());
          GamePlayer gamePlayer;

          if (game.getGamePlayers().stream().map(gp -> gp.getPlayer().getUserName()).collect(Collectors.toList()).contains(player.getUserName())){
            return new ResponseEntity<>("You are already in that game", HttpStatus.FORBIDDEN);
          }

          gamePlayer =  new GamePlayer(new Date(), player, game);
          gamePlayerRepository.save(gamePlayer);
          response = new ResponseEntity<>(makeMap("gpid", gamePlayer.getGamePlayerId()), HttpStatus.CREATED);
        }
      }
      else {
        response = new ResponseEntity<>("The game does not exist", HttpStatus.FORBIDDEN);
      }
    }
    return response;
  }

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

  private Map<String, Object>makeMap(String key, Object value){
    Map<String, Object> map = new HashMap<>();
    map.put(key,value);
    return map;
  }

  @RequestMapping(path = "/games/players/{gamePlayerid}/ships", method = RequestMethod.POST)
  public ResponseEntity<Object> addShips(@PathVariable long gamePlayerid, Authentication authentication, @RequestBody List<Ship> ships) {

    Optional<GamePlayer> OPTgamePlayer = gamePlayerRepository.findById(gamePlayerid);
    ResponseEntity<Object> response;

    if (this.isGuest(authentication)) {
      response = new ResponseEntity<>(makeMap("error", "You need to login!"), HttpStatus.UNAUTHORIZED);
    }
    else {
      Player currentUser = playerRepository.findByUserName(authentication.getName());

      if (!OPTgamePlayer.isPresent()) {
        response = new ResponseEntity<>(makeMap("error", "There is no such game!"), HttpStatus.NOT_FOUND);
      } else if (OPTgamePlayer.get().getPlayer().getPlayerId() != currentUser.getPlayerId()){
          response = new ResponseEntity<>(makeMap("error", "You don't belong in this game!"), HttpStatus.UNAUTHORIZED);
      } else if (OPTgamePlayer.get().getShips().size() > 0) {
        response = new ResponseEntity<>(makeMap("error", "You have already placed ships!"), HttpStatus.FORBIDDEN);
      } else if (ships == null || ships.size() != 5) {
        response = new ResponseEntity<>(makeMap("error", "You need to place 5 ships!"), HttpStatus.FORBIDDEN);
      } else {
        GamePlayer gamePlayer = OPTgamePlayer.get();

        ships.forEach(ship -> gamePlayer.addShip(new Ship(ship.getShipType(), ship.getLocations(), gamePlayer)));
        shipRepo.saveAll(ships);
        gamePlayerRepository.save(gamePlayer);

        response = new ResponseEntity<>(makeMap("success", "The ships have been placed!"), HttpStatus.CREATED);

      }
    }
    return response;
  }
  @RequestMapping(path = "/games/players/{gamePlayerid}/salvoes", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<Object> addSalvoes(@PathVariable long gamePlayerid, Authentication authentication, @RequestBody List<String> locations) {
    Optional<GamePlayer> OPTgamePlayer = gamePlayerRepository.findById(gamePlayerid);
    ResponseEntity<Object> response;
    Player currentUser = playerRepository.findByUserName(authentication.getName());

    if(this.isGuest(authentication)){
      response = new ResponseEntity<>(makeMap("error", "You need to login!"), HttpStatus.UNAUTHORIZED);
    } else if(!OPTgamePlayer.isPresent()){
      response = new ResponseEntity<>(makeMap("error", "There is no such game!"), HttpStatus.FORBIDDEN);
    }else if(locations.size() != 5){
      response = new ResponseEntity<>(makeMap("error", "You can only place 5 salvoes!"), HttpStatus.FORBIDDEN);
    }else if (OPTgamePlayer.get().getPlayer().getPlayerId() != currentUser.getPlayerId()) {
      response = new ResponseEntity<>(makeMap("error", "You cannot see your opponent's salvoes!"), HttpStatus.FORBIDDEN);
    }else{
      GamePlayer gamePlayer = OPTgamePlayer.get();
      int turn = gamePlayer.getSalvoes().size()+1;
      Salvo salvo = new Salvo(turn, locations, gamePlayer);
      gamePlayer.addSalvo(salvo);
      gamePlayerRepository.save(gamePlayer);

      response = new ResponseEntity<>(makeMap("success","Salvoes were saved succesfully!"), HttpStatus.CREATED);

    }
    return response;
  }



}

