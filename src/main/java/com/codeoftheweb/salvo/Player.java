package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;


    public Player() { }

    public Player(String user) {
        this.userName = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public long getPlayerId() {
        return id;
    }

    @JsonIgnore
    public Map<String, Object> makeGameDTO_Player (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getPlayerId());
        dto.put("user_name", this.getUserName());
        return dto;
    }

    public List<Game> getGame() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }
}