package com.mygdx.shapewars.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.network.data.InputRequest;

public class ServerListener extends Listener {

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
            System.out.println(inputRequest.uuid.toString() + " " + inputRequest.directionInput + " " + inputRequest.valueInput);
        }
    }
}
