package com.mygdx.shapewars.network.client;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;
import java.io.IOException;

public class ClientConnector {

    private Client client;
    private Kryo kryo;

    public ClientConnector(ShapeWarsModel model) {
       this(model, "10.22.10.149");
    } // todo get ip address from client screen

    public ClientConnector(ShapeWarsModel model, String ipAddress) {
        this.client = new com.esotericsoftware.kryonet.Client();
        this.client.start();

        try {
            client.connect(5000, ipAddress, 25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        kryo = client.getKryo();

        // register messages
        this.kryo.register(InputRequest.class);
        this.kryo.register(GameResponse.class);

        // register components
        this.kryo.register(PositionComponent.class);
        this.kryo.register(VelocityComponent.class);
        this.kryo.register(HealthComponent.class);
        this.kryo.register(PositionComponent[].class);
        this.kryo.register(VelocityComponent[].class);
        this.kryo.register(HealthComponent[].class);

        // register helper classes
        this.kryo.register(Array.class);
        this.kryo.register(Vector2.class);
        this.kryo.register(float[].class);

        client.addListener(new ClientListener(model));
    }

    public void sendInput(String clientId, int valueInput, int directionInput) {
        client.sendUDP(new InputRequest(clientId, valueInput, directionInput));
    }
}
