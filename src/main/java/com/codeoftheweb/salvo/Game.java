package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime gameDate;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;


    public Game() { }

    //Constructor

    public Game(LocalDateTime gameDate){
        this.gameDate = gameDate;
    }

    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
    }

    @JsonIgnore
    public List<Player> getPlayer() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public long getId() {
        return id;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }


    public Map<String, Object> createGameDTO(){
        Map<String, Object> gameDTO = new LinkedHashMap<>();
        gameDTO.put("id", this.id);
        gameDTO.put("created", this.getGameDate().atZone(ZoneId.systemDefault()).toInstant());
        gameDTO.put("gamePlayers", this.getGamePlayers().stream().map(GamePlayer::createGameDTO_GamePlayer));
        return gameDTO;
    }

}