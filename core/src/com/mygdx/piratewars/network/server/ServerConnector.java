package com.mygdx.piratewars.network.server;

import com.esotericsoftware.kryonet.Server;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.network.ConnectorStrategy;
import com.mygdx.piratewars.view.MainMenuView;
import java.io.IOException;

public class ServerConnector extends ConnectorStrategy {

    public Server server;
    private ServerListener listener;

    public ServerConnector(PirateWarsModel model) {
        this.server = new Server();
        this.server.start();
        this.listener = new ServerListener(model, this);
        this.server.addListener(listener);
        try {
            server.bind(25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
            model.controller.setScreen(new MainMenuView(model.controller));
            return;
        }

        this.kryo = server.getKryo();
        registerKryo();
    }

    @Override
    public void dispose() throws IOException {
        try {
            server.removeListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.close();
    }
}
