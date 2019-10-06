package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/leaderboard")
    public List<Object> getLeaderboard() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> player.toGameHistory())
                .collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public Map<String, Object> getAllGames() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("games", gameRepository
                .findAll()
                .stream()
                .map(Game::createGameDTO)
                .collect(toList()));
        return dto;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String,Object> getGameView(@PathVariable Long gamePlayerId) {

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        Map<String, Object> dto = gamePlayer.getGame().createGameDTO();
        dto.put("ships", gamePlayer.getShips().stream().map(Ship::createGameDTO_Ship));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream()
                .flatMap(gp->gp.getSalvoes().stream().map(Salvo::createGameDTO_Salvo))
                .collect(Collectors.toList()));

        return dto;

    }
}