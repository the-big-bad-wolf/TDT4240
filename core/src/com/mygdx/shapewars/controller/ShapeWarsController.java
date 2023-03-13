package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

            TiledMapTileLayer collisionLayer = shapeWarsView.getCollisionLayer();
            float tileSize = collisionLayer.getTileWidth();

            // calculate old and new position values
            float radians = MathUtils.degreesToRadians * velocityComponent.getDirection();

            float newX = positionComponent.getPosition().x + MathUtils.cos(radians) * velocityComponent.getSpeed();
            float newY = positionComponent.getPosition().y + MathUtils.sin(radians) * velocityComponent.getSpeed();

            // calculate the tank's bounding box
            Polygon tankBounds = new Polygon(new float[] {
                    0, 0,
                    spriteComponent.getSprite().getWidth(), 0,
                    spriteComponent.getSprite().getWidth(), spriteComponent.getSprite().getHeight(),
                    0, spriteComponent.getSprite().getHeight()
            });
            tankBounds.setOrigin(spriteComponent.getSprite().getOriginX(), spriteComponent.getSprite().getOriginY());
            tankBounds.setPosition(newX, newY);
            tankBounds.setRotation(velocityComponent.getDirection());

            // check for collision with walls
            for (int x = 0; x < collisionLayer.getWidth(); x++) {
                for (int y = 0; y < collisionLayer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                    if (cell != null) {
                        Rectangle rect = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
                        Polygon tileBounds = new Polygon(new float[] {
                                rect.x, rect.y, // bottom left corner
                                rect.x + rect.width, rect.y, // bottom right corner
                                rect.x + rect.width, rect.y + rect.height, // top right corner
                                rect.x, rect.y + rect.height // top left corner
                        });

                        if (Intersector.overlapConvexPolygons(tankBounds, tileBounds)) {
                            // tank collides with wall, adjust position
                            Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
                            Intersector.overlapConvexPolygons(tankBounds, tileBounds, mtv);
                            Vector2 overlapVector = new Vector2(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
                            newX += overlapVector.x;
                            newY += overlapVector.y;
                        }

                    }
                }
            }

            // update position and rotation
            positionComponent.addPosition(newX, newY);
            spriteComponent.getSprite().setPosition(newX, newY);
            spriteComponent.getHitbox().setPosition(newX, newY);
            spriteComponent.setRotation(velocityComponent.getDirection());
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
