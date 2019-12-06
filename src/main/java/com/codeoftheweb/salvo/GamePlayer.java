package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Salvo> salvoes = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Ship> ships = new HashSet<>();

    public GamePlayer() {
    }

    public GamePlayer(Date joinDate, Player player, Game game) {
        this.joinDate = joinDate;
        this.player = player;
        this.game = game;
    }

    public Set<Salvo> getSalvoes(){return salvoes;}

    public Set<Ship> getShips() {
        return ships;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public long getGamePlayerId() {
        return id;
    }

    public long getGameId() {
        return id;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void addShip(Ship ship){
        this.ships.add(ship);
    }

    public void addSalvo(Salvo salvo){
        this.salvoes.add(salvo);
    }

    public Map<String, Object> createGameDTO_GamePlayer() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gpid", this.getGamePlayerId());
        //dto.put("Player", this.getPlayer().createGameDTO_Player());
        dto.put("id", this.getPlayer().getPlayerId());
        dto.put("user", this.getPlayer().getUserName());
        return dto;
    }

    public Map<String, Object> dto_gameView (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getGameId());
        dto.put("created", this.getGame().getGameDate());
        dto.put("gamePlayers", this.game.getAllGamePlayers(this.game.getGamePlayers()));
        dto.put("ships", this.getShips().stream().map(Ship::createGameDTO_Ship));
        dto.put("salvoes", this.game.getGamePlayers().stream()
                .flatMap(gp->gp.getSalvoes().stream().map(Salvo::createGameDTO_Salvo))
                .collect(Collectors.toList()));
        dto.put("gameState", this.gameState());
        return dto;
    }

    private GamePlayer getEnemyGamePlayer(){
        GamePlayer enemyGamePlayer = this.getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getGamePlayerId() != this.getGamePlayerId()).findFirst().orElse(null);
        return enemyGamePlayer;
    }

    private long getGamePlayerSink(int turn, Set <Salvo> salvo, Set <Ship> ships){
        List<String> shoot = new LinkedList<>();
        List<String> allEnemyLoc = new ArrayList<>();
        long sinks = 0;


        salvo.stream().filter(e -> e.getTurn()<= turn).forEach(x -> shoot.addAll(x.getSalvoLocations()));

        sinks = ships.stream().filter(s ->shoot.containsAll(s.getLocations())).count();


        // shoot = this.getEnemyGamePlayer().getShips().stream().filter(ship -> allEnemyLoc.containsAll(ship.getLocations())).map(Ship::makeGameDTO_Ship).collect(Collectors.toList());

        return sinks; //retorna n° de sinks
    }

    private String gameState() {

        String gameState="";
        int turn = this.getSalvoes().size() + 1;
        int enemyTurn;
        long mysinks;
        long enemySinks;
        if(this.getEnemyGamePlayer() != null) {
            enemyTurn = this.getEnemyGamePlayer().getSalvoes().size() + 1;
            mysinks = this.getGamePlayerSink(turn, this.getSalvoes(), this.getEnemyGamePlayer().getShips());
            enemySinks = this.getGamePlayerSink(turn, this.getEnemyGamePlayer().getSalvoes(), this.getShips());


            if (this.getGamePlayerId() < this.getEnemyGamePlayer().getGamePlayerId()) {
                if(turn > enemyTurn){
                    gameState = "wait";
                }else if(turn == enemyTurn) {
                    if (this.getShips().size() < 5) {
                        gameState = "place ships"; //place ships
                    } else if (this.getShips().size() == 5 && this.getEnemyGamePlayer().getShips().size() < 5) {
                        gameState = "wait opponent ships"; //wait for enemy ships
                    } else {
                        gameState = "shoot";
                    }
                }
            }
            if (this.getGamePlayerId() > this.getEnemyGamePlayer().getGamePlayerId()) {
                if(turn < enemyTurn){
                    gameState = "shoot";
                }else if(turn == enemyTurn) {
                    if (this.getShips().size() < 5) {
                        gameState = "place ships"; //place ships
                    } else if (this.getShips().size() == 5 && this.getEnemyGamePlayer().getShips().size() < 5) {
                        gameState = "wait opponent ships"; //wait for enemy ships
                    } else {
                        gameState = "wait";
                    }
                }
            }
            if (mysinks == 5 && turn == enemyTurn) {
                gameState = "win"; //G.O Win
            } else if (enemySinks == 5 && turn == enemyTurn) {
                gameState = "lose"; //G.O Lose
            } else if (mysinks == 5 && enemySinks == 5 && enemyTurn == turn) {
                gameState = "tie"; //G.O tie
            }

        }else{ enemyTurn = 0;
            mysinks = 0;
            enemySinks = 0;

            if (this.getShips().size() < 5) {
                gameState = "place ships"; //place ships
            }else if (this.getShips().size() == 5 && this.getEnemyGamePlayer() == null) {
                gameState = "wait opponent"; //wait for enemy ships
            }
        }
        return gameState;
    }
}


