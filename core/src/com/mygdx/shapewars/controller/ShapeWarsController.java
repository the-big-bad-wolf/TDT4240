package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.mygdx.shapewars.view.JoinView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private final JoinView joinView;
    private Screen currentScreen;
    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView shapeWarsView, MainMenuView mainMenuView, JoinView joinView) {
      this.model = model;
      this.mainMenuView = mainMenuView;
      this.shapeWarsView = shapeWarsView;
      this.joinView = joinView;
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

    public JoinView getHostView() {
        return joinView;
    }
}
