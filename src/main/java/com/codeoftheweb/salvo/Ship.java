package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private ShipType shipType;
    public static boolean  checkShipsLength;
    private static Map<String, Integer> shipLengths;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="location")
    private List<String> locations = new ArrayList<>();

    public Ship() { }

    public Ship(ShipType shipType, List<String> locations, GamePlayer gamePlayer){
        this.shipType = shipType;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
    }



    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public List<String> getLocations() {
        return locations;
    }

    public Map<String, Object> createGameDTO_Ship (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("shipType", this.getShipType());
        dto.put("locations", this.getLocations());
        return dto;
    }

    private static Map<String, Integer> shipTypeLengths = Collections.unmodifiableMap(
            new HashMap<String, Integer>(){{
                put("CARRIER", 5);
                put("BATTLESHIP", 4);
                put("SUBMARINE",3);
                put("DESTROYER",3);
                put("PATROL_BOAT",2);
            }}
    );


    public static boolean areValid(List<Ship> ships) {
       boolean isOk = ships.stream().map(ship -> ship.getShipType()).distinct().count() == 5;

       int i = 0;

       while (isOk && i < ships.stream().count()){
           String shipType = ships.get(i).getShipType().name();
           Integer elNumeroCorrecto = shipTypeLengths.get(shipType);
           isOk = ships.get(i).locations.size() == elNumeroCorrecto;
           i++;
       }

       List<String> allLocations = ships.stream().flatMap(ship -> ship.locations.stream()).collect(toList());

       if(isOk){
           Integer totalCount = shipTypeLengths.values().stream().reduce((Integer x, Integer y) -> x + y).get();

           isOk = allLocations.size() == totalCount;
       }

       for(String celda : allLocations){

           if (isOk){

            if(!(celda instanceof String) || celda.length() < 2){
                return isOk = false;
            }

            char y = celda.substring(0,1).charAt(0);
            int x;

            try{
                x = Integer.parseInt(celda.substring(1));
            }catch(NumberFormatException e){
                x = 99;
            };

            if(x < 1 || x > 10 || y < 'A' || y > 'J'){
                return isOk = false;
            }
            }else{
               return isOk;
           }
       }

       return isOk;

    }

}