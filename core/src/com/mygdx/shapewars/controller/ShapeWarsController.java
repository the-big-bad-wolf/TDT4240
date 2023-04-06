package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.config.Role;
import com.mygdx.shapewars.model.GameModel;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController {

    public ShapeWarsModel shapeWarsModel;
    public final GameModel gameModel;
    private Screen currentScreen;
    public Launcher launcher;

    public ShapeWarsController(Launcher launcher) {
        this.gameModel = new GameModel(launcher);
        this.launcher = launcher;
    }

    public void generateShapeWarsModel(Role role, String serverIpAddress) {
        this.shapeWarsModel = new ShapeWarsModel(this.gameModel, role, serverIpAddress);
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
          shapeWarsModel.update();
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        gameModel.batch.dispose();
    }

    public void setScreen(Screen screen) {
        if (screen != null) {
            this.currentScreen = screen;
            this.currentScreen.show();
        }
    }
}
