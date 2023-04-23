package com.mygdx.piratewars.network.data;

public class GameResponse {
    public ShipData[] ships;
    public BulletData[] bullets;
    public boolean isGameActive;

    public GameResponse() {}

    public GameResponse(ShipData[] ships, BulletData[] bullets, Boolean isGameActive) {
        this.ships = ships;
        this.bullets = bullets;
        this.isGameActive = isGameActive;
    }
}
