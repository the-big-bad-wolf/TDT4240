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
import com.mygdx.shapewars.network.data.LobbyRequest;
import com.mygdx.shapewars.network.data.LobbyResponse;

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
        if (object instanceof LobbyRequest) {
            LobbyRequest request = (LobbyRequest) object;
            if (!model.deviceTankMapping.containsKey(request.deviceId))
                model.deviceTankMapping.put(request.deviceId, model.deviceTankMapping.size());
            connection.sendUDP(new LobbyResponse(model.deviceTankMapping.size(),
                    model.deviceTankMapping.get(request.deviceId), model.isGameActive));
        }

        if (object instanceof InputRequest) {
            InputRequest request = (InputRequest) object;
            Entity entity = model.engine.getEntities().get(model.deviceTankMapping.get(request.clientId));
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            velocityComponent.setVelocity(request.valueInput, request.directionInput);
            if (request.firingFlag)
                model.unshotBullets.add(entity); // cannot call firing system directly from this thread
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
            GameResponse response = new GameResponse(true, model.deviceTankMapping.size(), model.deviceTankMapping.get(request.clientId), velocityComponentsArray, positionComponentsArray, healthComponentsArray);
            connection.sendUDP(response);
        }
    }
}
