package com.mygdx.shapewars.network.data;

import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class GameResponse {

    public VelocityComponent[] velocityComponents;
    public PositionComponent[] positionComponents;

    public HealthComponent[] healthComponents;

    public GameResponse() {}

    public GameResponse(VelocityComponent[] velocityComponents, PositionComponent[] positionComponents, HealthComponent[] healthComponents) {
        this.velocityComponents = velocityComponents;
        this.positionComponents = positionComponents;
        this.healthComponents = healthComponents;
    }
}
