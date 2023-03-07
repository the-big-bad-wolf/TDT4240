package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

            float newX = model.x + MathUtils.cos(radians) * model.velocity;
            float newY = model.y + MathUtils.sin(radians) * model.velocity;

            Rectangle wallsRect = checkCollisionWithWalls(newX, newY, model.tankSprite.getWidth(), model.tankSprite.getHeight(), shapeWarsView.getCollisionLayer());

            if (wallsRect != null) {
                // adjust newX and newY based on collision direction
                if (model.x + model.tankSprite.getWidth() <= wallsRect.getX()) {
                    if (model.y + model.tankSprite.getHeight() <= wallsRect.getY()) {
                        newY = wallsRect.getY() - model.tankSprite.getHeight();
                    } else if (model.y >= wallsRect.getY() + wallsRect.getHeight()) {
                        newY = wallsRect.getY() + wallsRect.getHeight();
                    }
                    // left collision
                    newX = wallsRect.getX() - model.tankSprite.getWidth();
                } else if (model.x >= wallsRect.getX() + wallsRect.getWidth()) {
                    if (model.y + model.tankSprite.getHeight() <= wallsRect.getY()) {
                        newY = wallsRect.getY() - model.tankSprite.getHeight();
                    } else if (model.y >= wallsRect.getY() + wallsRect.getHeight()) {
                        newY = wallsRect.getY() + wallsRect.getHeight();
                    }
                    // right collision
                    newX = wallsRect.getX() + wallsRect.getWidth();
                } else if (model.y + model.tankSprite.getHeight() <= wallsRect.getY()) {
                    // top collision
                    newY = wallsRect.getY() - model.tankSprite.getHeight();
                } else if (model.y >= wallsRect.getY() + wallsRect.getHeight()) {
                    // bottom collision
                    newY = wallsRect.getY() + wallsRect.getHeight();
                }
                // set new position
            }
            model.x = newX;
            model.y = newY;
            model.tankSprite.setPosition(model.x, model.y);

        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }

        currentScreen.render(0);
    }

    private Rectangle checkCollisionWithWalls(float x, float y, float width, float height, TiledMapTileLayer wallsLayer) {
        for (int col = 0; col < wallsLayer.getWidth(); col++) {
            for (int row = 0; row < wallsLayer.getHeight(); row++) {
                TiledMapTileLayer.Cell cell = wallsLayer.getCell(col, row);
                if (cell != null) {
                    Rectangle rect = new Rectangle(col * wallsLayer.getTileWidth(), row * wallsLayer.getTileHeight(),
                            wallsLayer.getTileWidth(), wallsLayer.getTileHeight());
                    if (rect.overlaps(new Rectangle(x, y, width, height))) {
                        return rect;
                    }
                }
            }
        }
        return null;
    }

    public void dispose() {
        model.batch.dispose();
    }
}
