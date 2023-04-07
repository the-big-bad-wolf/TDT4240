package com.mygdx.shapewars.network.server;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;
import java.util.ArrayList;


public class ServerListener extends Listener {

    private ShapeWarsModel model;

    public ServerListener(ShapeWarsModel model) {
        this.model = model;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("New client connected");
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Client disconnected");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof InputRequest) {
            InputRequest inputRequest = (InputRequest) object;
            if (model.isGameActive) {
                Entity entity = model.engine.getEntities().get(model.deviceTankMapping.get(inputRequest.clientId));
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
                velocityComponent.setMagnitudeAndDirection(inputRequest.valueInput, inputRequest.directionInput);

                ArrayList<VelocityComponent> velocityComponentsNew = new ArrayList<>();
                ArrayList<PositionComponent> positionComponentsNew = new ArrayList<>();
                ArrayList<HealthComponent> healthComponentsNew = new ArrayList<>();
                for (Entity e : model.engine.getEntities()) {
                    velocityComponentsNew.add(ComponentMappers.velocity.get(e));
                    positionComponentsNew.add(ComponentMappers.position.get(e));
                    healthComponentsNew.add(ComponentMappers.health.get(e));
                }

                PositionComponent[] positionComponentsArray = positionComponentsNew.toArray(new PositionComponent[positionComponentsNew.size()]);
                VelocityComponent[] velocityComponentsArray = velocityComponentsNew.toArray(new VelocityComponent[velocityComponentsNew.size()]);
                HealthComponent[] healthComponentsArray = healthComponentsNew.toArray(new HealthComponent[healthComponentsNew.size()]);
                GameResponse response = new GameResponse(true, model.deviceTankMapping.size(), model.deviceTankMapping.get(inputRequest.clientId), velocityComponentsArray, positionComponentsArray, healthComponentsArray);
                connection.sendUDP(response);
            } else {
                if (!model.deviceTankMapping.containsKey(inputRequest.clientId))
                    model.deviceTankMapping.put(inputRequest.clientId, model.deviceTankMapping.size());
                GameResponse response = new GameResponse(false, 0, 0, null, null, null);
                connection.sendUDP(response);
            }
        }
    }
}
