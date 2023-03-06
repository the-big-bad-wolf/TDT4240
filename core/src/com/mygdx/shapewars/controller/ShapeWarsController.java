package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

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
            // get direction
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                model.direction += 2f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                model.direction -= 2f;
            }

            // get velocity
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                model.velocity = 5;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                model.velocity = -5;
            } else {
                model.velocity = 0;
            }

            // set direction
            model.tankSprite.setRotation(model.direction);

            // calculate and set position
            float radians = MathUtils.degreesToRadians * model.direction;
            model.x += MathUtils.cos(radians) * model.velocity;
            model.y += MathUtils.sin(radians) * model.velocity;
            model.tankSprite.setPosition(model.x, model.y);

            // check for borders
            if (model.x + model.tankSprite.getWidth() > Gdx.graphics.getWidth()) {
                model.x = Gdx.graphics.getWidth() - model.tankSprite.getWidth();
            } else if (model.x < 0) {
                model.x = 0;
            }
            if (model.y + model.tankSprite.getHeight() > Gdx.graphics.getHeight()) {
                model.y = Gdx.graphics.getHeight() - model.tankSprite.getHeight();
            } else if (model.y < 0) {
                model.y = 0;
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }

        currentScreen.render(0);
    }

    public void dispose() {
        model.batch.dispose();
    }
}
