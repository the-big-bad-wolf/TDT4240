package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private final VelocityComponent velocityComponent;
    private final PositionComponent positionComponent;
    private final SpriteComponent spriteComponent;
    
    private Screen currentScreen;
    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
      this.model = model;
      this.shapeWarsView = view;
      this.mainMenuView = mainMenuView;
      this.currentScreen = mainMenuView;
      velocityComponent = ComponentMappers.velocity.get(model.tank);
      positionComponent = ComponentMappers.position.get(model.tank);
      spriteComponent = ComponentMappers.sprite.get(model.tank);
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
            // get direction
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velocityComponent.addDirection(2);
              } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velocityComponent.addDirection(-2);
            }

            // get velocity
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocityComponent.setSpeed(5);
              } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocityComponent.setSpeed(-5);
              } else {
                velocityComponent.setSpeed(0);
            }

            // set direction
            spriteComponent.getSprite().setRotation(velocityComponent.getDirection());

            // calculate and set position
            float radians = MathUtils.degreesToRadians * velocityComponent.getDirection();

            float newX = positionComponent.getPosition().x + MathUtils.cos(radians) * velocityComponent.getSpeed();
            float newY = positionComponent.getPosition().y + MathUtils.sin(radians) * velocityComponent.getSpeed();

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
            }
            positionComponent.addPosition(newX, newY);
            spriteComponent.getSprite().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);

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
