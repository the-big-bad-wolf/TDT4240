package com.mygdx.piratewars.network.data;

public class GameResponse {
    public ShipData[] ships;
    public BulletData[] bullets;

    public GameResponse() {}

    public GameResponse(ShipData[] ships, BulletData[] bullets) {
        this.ships = ships;
        this.bullets = bullets;
    }
}
