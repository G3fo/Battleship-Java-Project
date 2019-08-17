package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime gameDate;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayers;


    public Game() { }


    public Game(LocalDateTime gameDate){
        this.gameDate = gameDate;
    }

    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
    }


}