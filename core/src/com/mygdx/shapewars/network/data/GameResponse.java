package com.mygdx.shapewars.network.data;

import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class GameResponse {

    public VelocityComponent[] velocityComponents;
    public PositionComponent[] positionComponents;

    public GameResponse() {}

    public GameResponse(VelocityComponent[] velocityComponents, PositionComponent[] positionComponents) {
        this.velocityComponents = velocityComponents;
        this.positionComponents = positionComponents;
    }
}
