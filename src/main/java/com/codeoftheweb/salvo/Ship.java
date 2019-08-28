package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
//  private ShipType shipType;

    public Ship() {
    }

    @ManyToOne(fetch = FetchType.EAGER);
    @JoinColumn(name = "gamePlayer_id");
    




}
