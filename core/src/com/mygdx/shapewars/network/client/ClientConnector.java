package com.mygdx.shapewars.network.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;
import java.io.IOException;
import java.util.UUID;

public class ClientConnector {

    private Client client;
    private Kryo kryo;

    public ClientConnector() {
       this("10.22.10.149");
    }

    public ClientConnector(String ipAddress) {
        this.client = new com.esotericsoftware.kryonet.Client();
        this.client.start();

        try {
            client.connect(5000, ipAddress, 25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        kryo = client.getKryo();
        kryo.register(InputRequest.class);
        kryo.register(GameResponse.class);
        kryo.register(UUID.class);

        client.addListener(new ClientListener());
    }

    public void sendInput(UUID clientId, int valueInput, int directionInput) {
        System.out.println("Sending inputs");
        client.sendUDP(new InputRequest(clientId, valueInput, directionInput));
    }
}
