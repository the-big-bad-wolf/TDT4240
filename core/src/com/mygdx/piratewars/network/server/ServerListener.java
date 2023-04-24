package com.mygdx.piratewars.network.server;

import static com.mygdx.piratewars.config.GameConfig.SHIP_FAMILY;
import static com.mygdx.piratewars.config.GameConfig.BULLET_FAMILY;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;
import com.mygdx.piratewars.network.data.BulletData;
import com.mygdx.piratewars.network.data.GameResponse;
import com.mygdx.piratewars.network.data.InputRequest;
import com.mygdx.piratewars.network.data.LobbyRequest;
import com.mygdx.piratewars.network.data.LobbyResponse;
import com.mygdx.piratewars.network.data.ShipData;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerListener extends Listener {
    public ServerConnector connector;
    private PirateWarsModel model;
    private HashMap<Integer, String> connectionDeviceMapping;

    public ServerListener(PirateWarsModel model, ServerConnector connector) {
        this.connector = connector;
        this.model = model;
        this.connectionDeviceMapping = new HashMap<>();
    }

    @Override
    public void connected(Connection connection) {
        if (connector.server.getConnections().length > 3) {
            connection.close();
        }
        System.out.println("New client connected");
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println(connectionDeviceMapping.get(connection.getID()));
        model.getDeviceShipMapping().remove(connectionDeviceMapping.get(connection.getID()));
        connectionDeviceMapping.remove(connection.getID());
        System.out.println("Client disconnected");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof LobbyRequest) {
            LobbyRequest request = (LobbyRequest) object;
            if (!model.getDeviceShipMapping().containsKey(request.deviceId)) {
                model.getDeviceShipMapping().put(request.deviceId, model.getDeviceShipMapping().size());
                connectionDeviceMapping.put(connection.getID(), request.deviceId);
            }
            connection.sendUDP(new LobbyResponse(model.getDeviceShipMapping().size(),
                    model.getDeviceShipMapping().get(request.deviceId), model.getSelectedMap(), model.isGameActive()));
        }

        if (object instanceof InputRequest) {
            // first get input from the client
            InputRequest request = (InputRequest) object;
            ImmutableArray<Entity> ships = model.getEngine().getEntitiesFor(SHIP_FAMILY);
            try {
                for (int i = 0; i < ships.size(); i++) {
                    if (ComponentMappers.identity.get(ships.get(i)).getId() == model.getDeviceShipMapping()
                            .get(request.clientId)) {
                        VelocityComponent velocityComponent = ComponentMappers.velocity.get(ships.get(i));
                        velocityComponent.setVelocity(request.valueInput, request.directionShipInput,
                                request.directionGunInput);
                        if (request.firingFlag) {
                            model.getUpdateSystemStrategy().addUnshotBullets(ships.get(i)); // cannot call firing system
                                                                                       // directly from this thread
                        }
                    }
                }
            } catch (NullPointerException e) {
                System.err.println(e);
            }

            ArrayList<ShipData> shipDataArrayList = new ArrayList<>();
            ArrayList<BulletData> bulletDataArrayList = new ArrayList<>();

            ImmutableArray<Entity> bullets = model.getEngine().getEntitiesFor(BULLET_FAMILY);

            for (int i = 0; i < ships.size(); i++) {
                Entity ship = ships.get(i);
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(ship);
                PositionComponent positionComponent = ComponentMappers.position.get(ship);
                HealthComponent healthComponent = ComponentMappers.health.get(ship);
                IdentityComponent identityComponent = ComponentMappers.identity.get(ship);
                shipDataArrayList
                        .add(new ShipData(velocityComponent, positionComponent, healthComponent, identityComponent));

            }
            for (int i = 0; i < bullets.size(); i++) {
                Entity bullet = bullets.get(i);
                VelocityComponent velocityComponent = ComponentMappers.velocity.get(bullet);
                PositionComponent positionComponent = ComponentMappers.position.get(bullet);
                HealthComponent healthComponent = ComponentMappers.health.get(bullet);
                bulletDataArrayList.add(new BulletData(velocityComponent, positionComponent, healthComponent));
            }
            ShipData[] shipDataArray = shipDataArrayList.toArray(new ShipData[shipDataArrayList.size()]);
            BulletData[] bulletDataArray = bulletDataArrayList.toArray(new BulletData[bulletDataArrayList.size()]);
            GameResponse response = new GameResponse(shipDataArray, bulletDataArray, model.isGameActive());
            connection.sendUDP(response);
        }
    }
}
