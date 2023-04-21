package com.mygdx.shapewars.network.server;

import com.esotericsoftware.kryonet.Server;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.network.ConnectorStrategy;
import com.mygdx.shapewars.view.MainMenuView;
import java.io.IOException;

public class ServerConnector extends ConnectorStrategy {

    private Server server;
    private ServerListener listener;

    public ServerConnector(ShapeWarsModel model) {
        this.server = new Server();
        this.server.start();
        this.listener = new ServerListener(model);
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
