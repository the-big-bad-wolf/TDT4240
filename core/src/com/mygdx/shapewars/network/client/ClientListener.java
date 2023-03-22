package com.mygdx.shapewars.network.client;
import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
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
            System.out.println(model.engine.getEntities().size());
            for (int i = 0; i < model.engine.getEntities().size(); i++) {
                Entity entity = model.engine.getEntities().get(i);
                entity.remove(PositionComponent.class);
                entity.add(((GameResponse) object).positionComponents[i]);
                entity.remove(VelocityComponent.class);
                entity.add(((GameResponse) object).velocityComponents[i]);

                float x = ((GameResponse) object).positionComponents[i].getPosition().x;
                float y = ((GameResponse) object).positionComponents[i].getPosition().y;
                float direction = ((GameResponse) object).velocityComponents[i].getDirection();

                ComponentMappers.sprite.get(entity).getSprite().setPosition(x, y);
                ComponentMappers.sprite.get(entity).getSprite().setRotation(direction);
            }
        }
    }
}
