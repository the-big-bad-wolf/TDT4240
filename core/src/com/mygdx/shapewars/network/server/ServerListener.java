package com.mygdx.shapewars.network.server;

import static com.mygdx.shapewars.config.GameConfig.TANK_FAMILY;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
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
            ImmutableArray<Entity> tanks = model.engine.getEntitiesFor(TANK_FAMILY);
            try {
                for (int i = 0; i < tanks.size(); i++) {
                    if (ComponentMappers.identity.get(tanks.get(i)).getId() == model.deviceTankMapping.get(request.clientId)) {
                        VelocityComponent velocityComponent = ComponentMappers.velocity.get(tanks.get(i));
                        velocityComponent.setVelocity(request.valueInput, request.directionInput);
                        if (request.firingFlag) {
                            model.updateSystemServer.unshotBullets.add(tanks.get(i)); // cannot call firing system directly from this thread
                        }
                    }
                }
            } catch (NullPointerException e) {
                System.err.println(e);
            }

            ArrayList<TankData> tankDataArrayList = new ArrayList<>();
            ArrayList<BulletData> bulletDataArrayList = new ArrayList<>();


            Family bulletFamily = Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class).exclude(IdentityComponent.class).get();
            ImmutableArray<Entity> bullets = model.engine.getEntitiesFor(bulletFamily);

            for (int i = 0; i < tanks.size(); i++) {
                Entity tank = tanks.get(i);
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(tank);
                PositionComponent positionComponent = ComponentMappers.position.get(tank);
                HealthComponent healthComponent = ComponentMappers.health.get(tank);
                IdentityComponent identityComponent = ComponentMappers.identity.get(tank);
                tankDataArrayList.add(new TankData(velocityComponent, positionComponent, healthComponent, identityComponent));


            }
            for (int i = 0; i < bullets.size(); i++) {
                Entity bullet = bullets.get(i);
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(bullet);
                PositionComponent positionComponent = ComponentMappers.position.get(bullet);
                HealthComponent healthComponent = ComponentMappers.health.get(bullet);
                bulletDataArrayList.add(new BulletData(velocityComponent, positionComponent, healthComponent));
            }
            TankData[] tankDataArray = tankDataArrayList.toArray(new TankData[tankDataArrayList.size()]);
            BulletData[] bulletDataArray = bulletDataArrayList.toArray(new BulletData[bulletDataArrayList.size()]);
            GameResponse response = new GameResponse(tankDataArray, bulletDataArray);
            connection.sendUDP(response);
        }
    }
}
