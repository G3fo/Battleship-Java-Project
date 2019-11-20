package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private int turn;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayer_id")
  private GamePlayer gamePlayer;

  @ElementCollection
  @Column(name="salvo_location")
  private List<String> salvoLocations = new ArrayList<>();

  public Salvo() {
  }

  public Salvo(int turn, List<String> salvoLocations, GamePlayer gamePlayer) {
    this.salvoLocations = salvoLocations;
    this.turn = turn;
    this.gamePlayer = gamePlayer;
  }

  public long getTurn() {
    return turn;
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public List<String> getSalvoLocations() {
    return salvoLocations;
  }

  public Map<String, Object> createGameDTO_Salvo (){
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("turn", this.getTurn());
    dto.put("player", this.getGamePlayer().getPlayer().getUserName());
    dto.put("game_player_id", this.getGamePlayer().getGamePlayerId());
    dto.put("locations", this.getSalvoLocations());
    return dto;
  }
}
