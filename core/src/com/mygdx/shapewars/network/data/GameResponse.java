package com.mygdx.shapewars.network.data;

public class GameResponse {

    public long time = System.currentTimeMillis();

    public GameResponse(long time) {
        this.time = time;
    }

    public GameResponse() {}
}
