package com.mygdx.shapewars.network.client;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.network.ConnectorStrategy;
import com.mygdx.shapewars.network.data.InputRequest;
import com.mygdx.shapewars.network.data.LobbyRequest;
import com.mygdx.shapewars.view.MainMenuView;
import java.io.IOException;

public class ClientConnector extends ConnectorStrategy {

    private Client client;
    private ClientListener listener;

    public ClientConnector(ShapeWarsModel model, String ipAddress) {
        this.client = new com.esotericsoftware.kryonet.Client();
        this.client.start();

        try {
            client.connect(5000, ipAddress, 25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
            model.controller.setScreen(new MainMenuView(model.controller));
            return;
        }

        kryo = client.getKryo();
        registerKryo();

        this.listener = new ClientListener(model);
        client.addListener(listener);
    }

    @Override
    public void sendInputRequest(String clientId, float valueInput, float directionShipInput, float directionGunInput, boolean firingFlag) {
        client.sendUDP(new InputRequest(clientId, valueInput, directionShipInput, directionGunInput, firingFlag));
    }

    @Override
    public void sendLobbyRequest(String clientId) {
        client.sendUDP(new LobbyRequest(clientId));
    }

    @Override
    public void dispose() {
        client.removeListener(listener);
        client.close();
    }
}
