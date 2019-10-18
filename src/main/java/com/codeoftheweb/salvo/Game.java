package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date gameDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<Score> scores = new HashSet();

    public Game() {
    }

    public Game(Date gameDate) {
        this.gameDate = gameDate;
    }

    /*public Game(int hours){
        this.gameDate = new Date().toInstant().plusSeconds(3600 * hours);
    }*/

    public Date getGameDate() {
        return gameDate;
    }

    public long getGameId() {
        return id;
    }

    @JsonIgnore
    public List<Player> getPlayer() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Map<String, Object> createGameDTO() {
        Map<String, Object> gameDTO = new LinkedHashMap<>();
        gameDTO.put("id", this.getGameId());
        gameDTO.put("created", this.getGameDate().getTime());
        gameDTO.put("players", this.getGamePlayers().stream().map(GamePlayer::createGameDTO_GamePlayer));
        return gameDTO;
    }

    @JsonIgnore
    public List<Map <String, Object>> getAllGamePlayers (Set <GamePlayer> gamePlayers){
        return gamePlayers.stream().map(GP -> GP.createGameDTO_GamePlayer()).collect(Collectors.toList());
    }

}