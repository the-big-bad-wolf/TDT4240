package com.mygdx.shapewars.network.server;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;

import java.io.IOException;

public class ServerConnector {

    private Server server;
    private Kryo kryo;

    public ServerConnector(ShapeWarsModel model) {
        this.server = new Server();
        this.server.start();
        this.kryo = kryo;

        try {
            server.bind(25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.kryo = server.getKryo();

        // register messages
        this.kryo.register(InputRequest.class);
        this.kryo.register(GameResponse.class);

        // register components
        this.kryo.register(PositionComponent.class);
        this.kryo.register(VelocityComponent.class);
        this.kryo.register(PositionComponent[].class);
        this.kryo.register(VelocityComponent[].class);

        // register helper classes
        this.kryo.register(Array.class);
        this.kryo.register(Vector2.class);
        this.kryo.register(float[].class);


        this.server.addListener(new ServerListener(model));
    }
}
