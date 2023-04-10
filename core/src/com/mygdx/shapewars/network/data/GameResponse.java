package com.mygdx.shapewars.network.data;

public class GameResponse {
    public TankData[] tanks;
    public BulletData[] bullets;

    public GameResponse() {}

    public GameResponse(TankData[] tanks, BulletData[] bullets) {
        this.tanks = tanks;
        this.bullets = bullets;
    }
}
