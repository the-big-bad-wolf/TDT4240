package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
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
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.badlogic.gdx.Net.Protocol;
import com.mygdx.shapewars.view.HostView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private final HostView hostView;
    private Screen currentScreen;
    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView shapeWarsView, MainMenuView mainMenuView, HostView hostView) {
      this.model = model;
      this.mainMenuView = mainMenuView;
      this.shapeWarsView = shapeWarsView;
      this.hostView = hostView;
      this.currentScreen = mainMenuView;
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
          model.update();
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        model.batch.dispose();
    }

    public void setScreen(Screen screen) {
        if (screen != null) {
            if (this.currentScreen != screen) {
                this.currentScreen = screen;
                this.currentScreen.show();
            }
        }
    }
    public MainMenuView getMainMenuView () {
        return mainMenuView;
    }

    public ShapeWarsView getShapeWarsView() {
        return shapeWarsView;
    }

    public HostView getHostView() {
        return hostView;
    }
}
