package com.mygdx.shapewars.network.client;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.GameResponse;


public class ClientListener extends Listener {

    private ShapeWarsModel model;

    public ClientListener(ShapeWarsModel model) {
        this.model = model;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Client connected");
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Client disconnected");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameResponse) {
            GameResponse gameResponse = (GameResponse) object;
            if (gameResponse.isGameActive) {
                if (!model.isGameActive) {
                    model.numPlayers = gameResponse.numPlayers;
                    model.tankId = gameResponse.clientTankId;
                    model.isGameActive = true;
                    model.flag = true;
                }
                for (int i = 0; i < model.engine.getEntities().size(); i++) {
                    Entity entity = model.engine.getEntities().get(i);

                    // update position
                    PositionComponent[] positionComponents = gameResponse.positionComponents;
                    float x = positionComponents[i].getPosition().x;
                    float y = positionComponents[i].getPosition().y;
                    PositionComponent positionComponent = ComponentMappers.position.get(entity);
                    positionComponent.setPosition(x, y);

                    // update velocity
                    VelocityComponent[] velocityComponents = gameResponse.velocityComponents;
                    float direction = velocityComponents[i].getDirection();
                    float magnitude = velocityComponents[i].getValue();
                    VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
                    velocityComponent.setVelocity(magnitude, direction);

                    // update health
                    HealthComponent[] healthComponents = gameResponse.healthComponents;
                    int health = healthComponents[i].getHealth();
                    HealthComponent healthComponent = ComponentMappers.health.get(entity);
                    healthComponent.setHealth(health);
                }
            }
        }
    }
}
