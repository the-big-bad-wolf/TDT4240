package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private Screen currentScreen;
    private MovementSystem movementSystem;

    //private final VelocityComponent velocityComponent;


    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
      this.model = model;
      this.shapeWarsView = view;
      this.mainMenuView = mainMenuView;
      this.currentScreen = mainMenuView;
      movementSystem = movementSystem.getInstance();
      //velocityComponent = ComponentMappers.velocity.get(model.tank);
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
          model.update();
/*
            Rectangle wallsRect = checkCollisionWithWalls(newX, newY, spriteComponent.getSprite().getWidth(), spriteComponent.getSprite().getHeight(), shapeWarsView.getCollisionLayer());

            if (wallsRect != null) {
                // adjust newX and newY based on collision direction
                if (positionComponent.getPosition().x + spriteComponent.getSprite().getWidth() <= wallsRect.getX()) {
                    if (positionComponent.getPosition().y + spriteComponent.getSprite().getHeight() <= wallsRect.getY()) {
                        newY = wallsRect.getY() - spriteComponent.getSprite().getHeight();
                    } else if (positionComponent.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
                        newY = wallsRect.getY() + wallsRect.getHeight();
                    }
                    // left collision
                    newX = wallsRect.getX() - spriteComponent.getSprite().getWidth();
                } else if (positionComponent.getPosition().x >= wallsRect.getX() + wallsRect.getWidth()) {
                    if (positionComponent.getPosition().y + spriteComponent.getSprite().getHeight() <= wallsRect.getY()) {
                        newY = wallsRect.getY() - spriteComponent.getSprite().getHeight();
                    } else if (positionComponent.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
                        newY = wallsRect.getY() + wallsRect.getHeight();
                    }
                    // right collision
                    newX = wallsRect.getX() + wallsRect.getWidth();
                } else if (positionComponent.getPosition().y + spriteComponent.getSprite().getHeight() <= wallsRect.getY()) {
                    // top collision
                    newY = wallsRect.getY() - spriteComponent.getSprite().getHeight();
                } else if (positionComponent.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
                    // bottom collision
                    newY = wallsRect.getY() + wallsRect.getHeight();
                }
                // set new position
            } */
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }

        currentScreen.render(Gdx.graphics.getDeltaTime());
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
