package com.mygdx.piratewars.network;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
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

import java.io.IOException;

public abstract class ConnectorStrategy {
    protected Kryo kryo;

    public void registerKryo() {
        // register messages
        this.kryo.register(InputRequest.class);
        this.kryo.register(GameResponse.class);
        this.kryo.register(LobbyRequest.class);
        this.kryo.register(LobbyResponse.class);

        // register containers
        this.kryo.register(BulletData.class);
        this.kryo.register(ShipData.class);
        this.kryo.register(BulletData[].class);
        this.kryo.register(ShipData[].class);

        // register components
        this.kryo.register(PositionComponent.class);
        this.kryo.register(VelocityComponent.class);
        this.kryo.register(HealthComponent.class);
        this.kryo.register(IdentityComponent.class);
        this.kryo.register(PositionComponent[].class);
        this.kryo.register(VelocityComponent[].class);
        this.kryo.register(HealthComponent[].class);
        this.kryo.register(IdentityComponent[].class);

        // register helper classes
        this.kryo.register(Array.class);
        this.kryo.register(Vector2.class);
        this.kryo.register(float[].class);
    }

    public void sendInputRequest(String clientId, float valueInput, float directionShipInput, float directionGunInput, boolean firingFlag) {
        return;
    }

    public void sendLobbyRequest(String clientId) {
        return;
    }

    public abstract void dispose() throws IOException;
}
