package com.mygdx.piratewars.network.client;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.network.ConnectorStrategy;
import com.mygdx.piratewars.network.data.InputRequest;
import com.mygdx.piratewars.network.data.LobbyRequest;
import com.mygdx.piratewars.view.MainMenuView;
import java.io.IOException;

public class ClientConnector extends ConnectorStrategy {

    public Client client;
    public ClientListener listener;

    public ClientConnector(PirateWarsModel model, String ipAddress) {
        this.client = new com.esotericsoftware.kryonet.Client();
        this.client.start();

        try {
            client.connect(5000, ipAddress, 25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
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
