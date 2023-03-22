package com.mygdx.shapewars.network.data;

import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class GameResponse {

    public long time = System.currentTimeMillis();

    public VelocityComponent[] velocityComponents;
    public PositionComponent[] positionComponents;

    public GameResponse(long time) {
        this.time = time;
    }

    public GameResponse() {}

    public GameResponse(VelocityComponent[] velocityComponents, PositionComponent[] positionComponents) {
        this.velocityComponents = velocityComponents;
        this.positionComponents = positionComponents;
    }
}
