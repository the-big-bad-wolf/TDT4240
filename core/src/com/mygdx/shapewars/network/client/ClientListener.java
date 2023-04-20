package com.mygdx.shapewars.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.LobbyResponse;


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
        if (object instanceof LobbyResponse) {
            LobbyResponse response = (LobbyResponse) object;
            model.numPlayers = response.numPlayers;
            model.shipId = response.clientShipId;
            model.selectedMap = response.selectedMap;
            // the ordering here is important!
            model.createEntitiesFlag = !model.isGameActive && response.isGameActive;
            model.isGameActive = response.isGameActive;
        }
        if (object instanceof GameResponse) {
            GameResponse response = (GameResponse) object;
            if (!model.updateSystemClient.updated) {
                model.updateSystemClient.shipsServer = response.ships;
                model.updateSystemClient.bulletsServer = response.bullets;
                model.updateSystemClient.updated = true;
            }
        }
    }
}
