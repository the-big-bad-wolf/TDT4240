package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.mygdx.shapewars.view.HostView;

public class ShapeWarsController {

    public final ShapeWarsModel model;
    private Screen currentScreen;

    public Launcher launcher;
    
    public ShapeWarsController(Launcher launcher) {
      this.model = new ShapeWarsModel(launcher);
      this.launcher = launcher;
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
            this.currentScreen = screen;
            this.currentScreen.show();
        }
    }
}
