package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.badlogic.gdx.Net.Protocol;

import java.io.IOException;
import java.io.InputStream;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private Screen currentScreen;
    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
      this.model = model;
      this.shapeWarsView = view;
      this.mainMenuView = mainMenuView;
      this.currentScreen = mainMenuView;
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
          model.update();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                ServerSocketHints hints = new ServerSocketHints();
                hints.acceptTimeout=0;
                ServerSocket server = Gdx.net.newServerSocket(Protocol.TCP,5000,hints);
                Socket acceptedsocket = server.accept(null);
                try {
                    InputStream stream = acceptedsocket.getInputStream();
                    System.out.println(stream.read());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                currentScreen = shapeWarsView;
                currentScreen.show();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.U)) {
                SocketHints hints = new SocketHints();
                Socket socket = Gdx.net.newClientSocket(Protocol.TCP, "10.0.0.17", 5000, hints);
                try{
                    socket.getOutputStream().write(99);}
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        model.batch.dispose();
    }
}
