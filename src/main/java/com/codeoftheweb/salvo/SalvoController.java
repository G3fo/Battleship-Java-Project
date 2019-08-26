package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    //@Autowired
    //private GamePlayerRepository gamePlayerRepository;
    //@Autowired
    //private PlayerRepository playerRepository;


    //public List<Object> getAllGames(){
    //    return gameRepository.findAll().stream().map(game -> game.getId()).collect(toList());
    //}

    @RequestMapping("/games")
    public Map<String, Object> getAllGames() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("games", gameRepository.findAll().stream().map(Game::createGameDTO).collect(toList()));
        return dto;
    }

}