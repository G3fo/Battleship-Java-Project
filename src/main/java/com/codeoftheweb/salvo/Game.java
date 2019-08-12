package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    public Date gameDate = new Date();

    public Game() { }

    public void setGameDate(Date gameDate) {
        this.gameDate = LocalDate.parse("2011-08-03T03:15:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);;
    }

    public Date getGameDate() {
        return gameDate;
    }

}