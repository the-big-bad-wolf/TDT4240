package com.mygdx.shapewars.network.server;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.BulletData;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;
import com.mygdx.shapewars.network.data.LobbyRequest;
import com.mygdx.shapewars.network.data.LobbyResponse;
import com.mygdx.shapewars.network.data.TankData;
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
            // first get input from the client
            InputRequest request = (InputRequest) object;
            try {
                for (Entity e : model.engine.getEntities()) {
                    if (ComponentMappers.identity.get(e).getId() == model.deviceTankMapping.get(request.clientId)) {
                        VelocityComponent velocityComponent = ComponentMappers.velocity.get(e);
                        velocityComponent.setVelocity(request.valueInput, request.directionInput);
                        if (request.firingFlag) {
                            model.updateSystemServer.unshotBullets.add(e); // cannot call firing system directly from this thread
                        }
                    }
                }
            } catch (NullPointerException e) {}

            // todo new try
            ArrayList<TankData> tanks = new ArrayList<>();
            ArrayList<BulletData> bullets = new ArrayList<>();
            ImmutableArray<Entity> entities = model.engine.getEntities();


            // Family tankFamily = Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class, IdentityComponent.class).get();
            // ImmutableArray<Entity> tanksClient = model.engine.getEntitiesFor(tankFamily);


            for (Entity entity : entities) {
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
                PositionComponent positionComponent = ComponentMappers.position.get(entity);
                HealthComponent healthComponent = ComponentMappers.health.get(entity);
                IdentityComponent identityComponent = ComponentMappers.identity.get(entity);

                if (identityComponent != null) {
                    // identity component exists -> tank
                    tanks.add(new TankData(velocityComponent, positionComponent, healthComponent, identityComponent));
                } else {
                    bullets.add(new BulletData(velocityComponent, positionComponent, healthComponent));
                }

            }
            System.out.println("number of tanks im sending back: " + tanks.size());
            TankData[] tankDataArray = tanks.toArray(new TankData[tanks.size()]);
            BulletData[] bulletDataArray = bullets.toArray(new BulletData[bullets.size()]);
            GameResponse response = new GameResponse(tankDataArray, bulletDataArray);
            connection.sendUDP(response);
        }
    }
}
