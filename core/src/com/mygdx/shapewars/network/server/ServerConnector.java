package com.mygdx.shapewars.network.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;

import java.io.IOException;

public class ServerConnector {

    private Server server;
    private Kryo kryo;

    public ServerConnector() {
        this.server = new Server();
        this.server.start();
        this.kryo = kryo;

        try {
            server.bind(25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.kryo = server.getKryo();
        this.kryo.register(InputRequest.class);
        this.kryo.register(GameResponse.class);

        this.server.addListener(new ServerListener());

    }
}
