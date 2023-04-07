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
import com.mygdx.shapewars.network.data.LobbyResponse;


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
        if (object instanceof LobbyResponse) {
            LobbyResponse response = (LobbyResponse) object;
            model.numPlayers = response.numPlayers;
            model.tankId = response.clientTankId;
            // the ordering here is important!
            model.createEntitiesFlag = !model.isGameActive && response.isGameActive;
            model.isGameActive = response.isGameActive;
        }
        if (object instanceof GameResponse) {
            GameResponse response = (GameResponse) object;
            for (int i = 0; i < model.engine.getEntities().size(); i++) {
                Entity entity = model.engine.getEntities().get(i);

                // update position
                PositionComponent[] positionComponents = response.positionComponents;
                float x = positionComponents[i].getPosition().x;
                float y = positionComponents[i].getPosition().y;
                PositionComponent positionComponent = ComponentMappers.position.get(entity);
                positionComponent.setPosition(x, y);

                // update velocity
                VelocityComponent[] velocityComponents = response.velocityComponents;
                float direction = velocityComponents[i].getDirection();
                float magnitude = velocityComponents[i].getValue();
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
                velocityComponent.setVelocity(magnitude, direction);

                // update health
                HealthComponent[] healthComponents = response.healthComponents;
                int health = healthComponents[i].getHealth();
                HealthComponent healthComponent = ComponentMappers.health.get(entity);
                healthComponent.setHealth(health);
            }
        }
    }
}
