package com.mygdx.shapewars.network.server;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
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
            if (!model.clientTankMapping.containsKey(inputRequest.clientId)) {
                model.clientTankMapping.put(inputRequest.clientId, 1); // change to multiple
            }

            System.out.println(inputRequest.directionInput + " " + inputRequest.valueInput);
            Entity entity = model.engine.getEntities().get(model.clientTankMapping.get(inputRequest.clientId));
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            velocityComponent.setVelocity(inputRequest.valueInput, inputRequest.directionInput);

            ArrayList<VelocityComponent> velocityComponentsNew = new ArrayList<>();
            ArrayList<PositionComponent> positionComponentsNew = new ArrayList<>();
            for (Entity e : model.engine.getEntities()) {
                velocityComponentsNew.add(ComponentMappers.velocity.get(e));
                positionComponentsNew.add(ComponentMappers.position.get(e));
            }

            PositionComponent[] positionComponentsArray = positionComponentsNew.toArray(new PositionComponent[positionComponentsNew.size()]);
            VelocityComponent[] velocityComponentsArray = velocityComponentsNew.toArray(new VelocityComponent[velocityComponentsNew.size()]);
            GameResponse response = new GameResponse(velocityComponentsArray, positionComponentsArray);
            connection.sendUDP(response);
        }
    }
}
