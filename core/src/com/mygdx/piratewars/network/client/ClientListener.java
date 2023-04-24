package com.mygdx.piratewars.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.network.data.GameResponse;
import com.mygdx.piratewars.network.data.LobbyResponse;

public class ClientListener extends Listener {

    private PirateWarsModel model;

    public ClientListener(PirateWarsModel model) {
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
            model.setNumPlayers(response.numPlayers);
            model.setShipId(response.clientShipId);
            model.setSelectedMap(response.selectedMap);
            // the ordering here is important!

            if (!model.isGameActive() && response.isGameActive) {
                model.setCreateEntitiesFlag(true);
            }
            model.setGameActive(response.isGameActive);
        }
        if (object instanceof GameResponse) {
            GameResponse response = (GameResponse) object;
            if (!model.getUpdateSystemStrategy().updated) {
                model.getUpdateSystemStrategy().replaceData(response.ships, response.bullets, response.isGameActive);
                model.getUpdateSystemStrategy().updated = true;
            }
        }
    }
}
