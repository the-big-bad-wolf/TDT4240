package com.mygdx.shapewars.network.data;

import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class GameResponse {

    public boolean isGameActive;
    public int numPlayers;
    public int clientTankId;
    public VelocityComponent[] velocityComponents;
    public PositionComponent[] positionComponents;

    public HealthComponent[] healthComponents;

    public GameResponse() {}

    public GameResponse(boolean isGameActive, int numPlayers, int clientTankId, VelocityComponent[] velocityComponents, PositionComponent[] positionComponents, HealthComponent[] healthComponents) {
        this.isGameActive = isGameActive;
        this.numPlayers = numPlayers;
        this.clientTankId = clientTankId;
        this.velocityComponents = velocityComponents;
        this.positionComponents = positionComponents;
        this.healthComponents = healthComponents;
    }
}
