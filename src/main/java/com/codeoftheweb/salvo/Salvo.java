package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

  private GamePlayer getOpponentGamePlayer(){
    GamePlayer opponentGamePlayer = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getGamePlayerId() != this.getGamePlayer().getGamePlayerId()).findFirst().orElse(null);
    return opponentGamePlayer;
  }

  private List<String> getHits(){
    List<String> hitShips;
    if (this.getOpponentGamePlayer() != null)

      hitShips = this.getSalvoLocations().stream().filter(location -> getOpponentGamePlayer().getShips().stream().anyMatch(ship -> ship.getLocations().contains(location))).collect(Collectors.toList());

    else
      hitShips = null;
    return hitShips;
  }

  private List<Map<String,Object>> getSinks(){
    List<Map<String,Object>> sinkShips;
    List<String> allEnemyLocations = new ArrayList<>();
    this.getGamePlayer().getSalvoes().stream().filter(e -> e.getTurn()<= this.getTurn()).forEach(x -> allEnemyLocations.addAll(x.getSalvoLocations()));
    if (this.getOpponentGamePlayer() != null)

      sinkShips = this.getOpponentGamePlayer().getShips().stream().filter(ship -> allEnemyLocations.containsAll(ship.getLocations())).map(Ship::createGameDTO_Ship).collect(Collectors.toList());

    else
      sinkShips = null;
    return sinkShips;
  }

  public Map<String, Object> createGameDTO_Salvo (){
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("turn", this.getTurn());
    dto.put("player", this.getGamePlayer().getPlayer().getUserName());
    dto.put("game_player_id", this.getGamePlayer().getGamePlayerId());
    dto.put("locations", this.getSalvoLocations());
    dto.put("hits", this.getHits());
    dto.put("sinks", this.getSinks());
    return dto;
  }
}
