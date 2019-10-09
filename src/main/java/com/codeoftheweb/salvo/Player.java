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
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    Set<Score> scores = new HashSet();

    public Player() {
    }

    public Player(String user, String password) {
        this.userName = user;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public long getPlayerId() {
        return id;
    }

    @JsonIgnore
    public List<Game> getGame() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

    public float getWins() {
        return scores.stream().filter(score -> score.getScore() == 1).count();
    }

    public float getLoses() {
        return scores.stream().filter(score -> score.getScore() == 0).count();
    }

    public float getDraws() {
        return scores.stream().filter(score -> score.getScore() == 0.5).count();
    }

    public float getTotalScore() {
        return 1f * getWins() + 0.5f * getDraws() + 0 * getLoses();
    }

    public Map<String, Object> toGameHistory() {
        Map<String, Object> history = new LinkedHashMap<>();
        history.put("name", userName);
        history.put("total", getTotalScore());
        history.put("won", getWins());
        history.put("lost", getLoses());
        history.put("tied", getDraws());

        return history;
    }

    public Map<String, Object> createGameDTO_Player() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getPlayerId());
        dto.put("user", this.getUserName());
        return dto;
    }

    public Score getScore(Game game){
        return scores.stream().filter(score->score.getGame().getGameId() == game.getGameId()).findAny().orElse(null);
    }
}