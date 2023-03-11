package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            getDirectionAndVelocityInput();

            TiledMapTileLayer collisionLayer = shapeWarsView.getCollisionLayer();

            // calculate old and new position values
            float radians = MathUtils.degreesToRadians * velocityComponent.getDirection();

            float oldX = positionComponent.getPosition().x;
            float oldY = positionComponent.getPosition().y;

            float newX = positionComponent.getPosition().x + MathUtils.cos(radians) * velocityComponent.getSpeed();
            float newY = positionComponent.getPosition().y + MathUtils.sin(radians) * velocityComponent.getSpeed();

            // detect colliding corners and edges
            Polygon hitbox = spriteComponent.getHitbox();
            List<Vector2> collidingVertices = getCollidingWallVertices(positionComponent.getPosition().x, positionComponent.getPosition().y, spriteComponent.getHitbox(), shapeWarsView.getCollisionLayer());
            String collisionType = getCollisionType(newX, newY, hitbox, collisionLayer);
            float[] vertices = hitbox.getTransformedVertices();


            // set direction only if no collision with walls
            if (collidingVertices.isEmpty() && collisionType.equals("none")) {
                // no collision rotate the tank as planned
                spriteComponent.setRotation(velocityComponent.getDirection());
                // No collision, update the position as planned
                positionComponent.addPosition(newX, newY);
                spriteComponent.getSprite().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
                spriteComponent.getHitbox().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
            }
            else {
                if (!collisionType.equals("none")) {
                    velocityComponent.setSpeed(0);
                    System.out.println(collisionType);
                    switch (collisionType) {
                        case "left":
                            positionComponent.addPosition(oldX + 0.001f, oldY);
                            break;
                        case "right":
                            positionComponent.addPosition(oldX - 0.001f, oldY);
                            break;
                        case "top":
                            positionComponent.addPosition(oldX, oldY - 0.001f);
                            break;
                        case "bottom":
                            positionComponent.addPosition(newX, oldY + 0.001f);
                            break;
                        case "topLeft":
                            positionComponent.addPosition(oldX + 0.001f, oldY - 0.001f);
                            break;
                        case "topRight":
                            positionComponent.addPosition(oldX - 0.001f, oldY - 0.001f);
                            break;
                        case "bottomLeft":
                            positionComponent.addPosition(oldX + 0.001f, oldY + 0.001f);
                            break;
                        case "bottomRight":
                            positionComponent.addPosition(oldX - 0.001f, oldY + 0.001f);
                            break;
                    }
                }
                else {
                    // Handle the collision
                    velocityComponent.setSpeed(0);
                    // Initialize variables for collision detection
                    boolean left = false, right = false, up = false, down = false;
                    for (Vector2 vertex : collidingVertices) {
                        float x = vertex.x;
                        float y = vertex.y;
                        float centerX = hitbox.getX() + hitbox.getOriginX();
                        float centerY = hitbox.getY() + hitbox.getOriginY();
                        if (x < centerX && Math.abs(x - centerX) > Math.abs(y - centerY)) {
                            left = true;
                        } else if (x > centerX && Math.abs(x - centerX) > Math.abs(y - centerY)) {
                            right = true;
                        } else if (y < centerY && Math.abs(x - centerX) < Math.abs(y - centerY)) {
                            down = true;
                        } else if (y > centerY && Math.abs(x - centerX) < Math.abs(y - centerY)) {
                            up = true;
                        }
                    }

                    // Move the tank back to its previous position, using the appropriate old X or Y value
                    if (left && up) {
                        System.out.println("Collision from left and up");
                        positionComponent.addPosition(oldX + 0.001f, oldY - 0.001f);
                    } else if (left && down) {
                        System.out.println("Collision from left and down");
                        positionComponent.addPosition(oldX + 0.001f, oldY + 0.001f);
                    } else if (left) {
                        System.out.println("Collision from left");
                        positionComponent.addPosition(oldX + 0.001f, newY);
                    } else if (right && up) {
                        System.out.println("Collision from right and up");
                        positionComponent.addPosition(oldX - 0.001f, oldY - 0.001f);
                    } else if (right && down) {
                        System.out.println("Collision from right and down");
                        positionComponent.addPosition(oldX - 0.001f, oldY + 0.001f);
                    } else if (right) {
                        System.out.println("Collision from right");
                        positionComponent.addPosition(oldX - 0.001f, newY);
                    } else if (up) {
                        System.out.println("Collision from up");
                        positionComponent.addPosition(newX, oldY - 0.001f);
                    } else if (down) {
                        System.out.println("Collision from down ");
                        positionComponent.addPosition(newX, oldY + 0.001f);
                    }
                }


                spriteComponent.getSprite().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
                spriteComponent.getHitbox().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);


                boolean canRotate = true;
                // check if the rotation would cause a collision
                for (int i = 0; i < vertices.length; i += 2) {
                    float x = vertices[i];
                    float y = vertices[i + 1];
                    boolean left = false, right = false, up = false, down = false;
                    // check collision with each wall
                    for (Vector2 wallVertex : collidingVertices) {
                        float centerX = wallVertex.x;
                        float centerY = wallVertex.y;

                        // check which side of the wall the tank is on
                        if (x < centerX && Math.abs(x - centerX) > Math.abs(y - centerY)) {
                            left = true;
                        } else if (x > centerX && Math.abs(x - centerX) > Math.abs(y - centerY)) {
                            right = true;
                        } else if (y < centerY && Math.abs(y - centerY) > Math.abs(x - centerX)) {
                            down = true;
                        } else if (y > centerY && Math.abs(y - centerY) > Math.abs(x - centerX)) {
                            up = true;
                        }
                    }

                    // if the tank would collide with any wall, it can't rotate
                    if (left && velocityComponent.getDirection() == 90 || right && velocityComponent.getDirection() == 270 || up && velocityComponent.getDirection() == 180 || down && velocityComponent.getDirection() == 0) {
                        canRotate = false;
                        break;
                    }
                }

                if (canRotate) {
                    spriteComponent.getSprite().setRotation(velocityComponent.getDirection());
                    spriteComponent.getHitbox().setRotation(velocityComponent.getDirection());
                }
            }

        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }

        currentScreen.render(0);
    }

    private void getDirectionAndVelocityInput() {
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
    }
    public void dispose() {
        model.batch.dispose();
    }

    private List<Vector2> getCollidingWallVertices(Polygon hitbox, TiledMapTileLayer collisionLayer) {
        float[] vertices = hitbox.getTransformedVertices();
        List<Vector2> collidingVertices = new ArrayList<>();
        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];
            TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
            if (cell != null) {
                collidingVertices.add(new Vector2(x, y));
            }
        }
        return collidingVertices;
    }

    private List<Vector2> getCollidingWallVertices(float x, float y, Polygon hitbox, TiledMapTileLayer collisionLayer) {
        hitbox.setPosition(x, y);
        return getCollidingWallVertices(hitbox, collisionLayer);
    }


    public String getCollisionType(float x, float y, Polygon hitbox, TiledMapTileLayer collisionLayer) {
        // iterate over all cells in the collision layer that the hitbox touches
        int cellWidth = collisionLayer.getTileWidth();
        int cellHeight = collisionLayer.getTileHeight();
        int startX = (int) (x - hitbox.getBoundingRectangle().width / 2) / cellWidth;
        int startY = (int) (y - hitbox.getBoundingRectangle().height / 2) / cellHeight;
        int endX = (int) (x + hitbox.getBoundingRectangle().width / 2) / cellWidth;
        int endY = (int) (y + hitbox.getBoundingRectangle().height / 2) / cellHeight;

        System.out.println("startX: " + startX);
        System.out.println("startY: " + startY);
        System.out.println("endX: " + endX);
        System.out.println("endY: " + endY);

        boolean collidedTop = false;
        boolean collidedBottom = false;
        boolean collidedLeft = false;
        boolean collidedRight = false;

        // iterate over all cells in the collision layer that the hitbox touches
        for (int cellX = startX - 1; cellX <= endX + 1; cellX++) {
            for (int cellY = startY - 1; cellY <= endY + 1; cellY++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX, cellY);
                if (cell != null && cell.getTile() != null) {
                    // calculate the bounds of the cell
                    float cellLeft = cellX * cellWidth;
                    float cellBottom = cellY * cellHeight;
                    float cellRight = (cellX + 1) * cellWidth;
                    float cellTop = (cellY + 1) * cellHeight;

                    // create a polygon for the cell
                    Polygon cellPolygon = new Polygon(new float[]{cellLeft, cellBottom, cellRight, cellBottom, cellRight, cellTop, cellLeft, cellTop});

                    // check for overlap with the hitbox polygon
                    if (Intersector.overlapConvexPolygons(hitbox, cellPolygon)) {
                        // find the intersection points between the hitbox and the cell polygon
                        float[] cellVertices = cellPolygon.getTransformedVertices();
                        for (int i = 0; i < cellVertices.length; i += 2) {
                            float cellX1 = cellVertices[i];
                            float cellY1 = cellVertices[i + 1];
                            float cellX2 = cellVertices[(i + 2) % cellVertices.length];
                            float cellY2 = cellVertices[(i + 3) % cellVertices.length];

                            float hitboxTop = hitbox.getBoundingRectangle().y + hitbox.getBoundingRectangle().height;
                            float hitboxBottom = hitbox.getBoundingRectangle().y;
                            float hitboxLeft = hitbox.getBoundingRectangle().x;
                            float hitboxRight = hitbox.getBoundingRectangle().x + hitbox.getBoundingRectangle().width;


                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxLeft, hitboxTop, hitboxRight, hitboxTop, null)) {
                                collidedTop = true;
                            }
                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxLeft, hitboxBottom, hitboxRight, hitboxBottom, null)) {
                                collidedBottom = true;
                            }
                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxLeft, hitboxTop, hitboxLeft, hitboxBottom, null)) {
                                collidedLeft = true;
                            }
                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxRight, hitboxTop, hitboxRight, hitboxBottom, null)) {
                                collidedRight = true;
                            }

                        }
                    }
                }
            }
        }
        // determine the type of collision based on which sides were collided with
        if (collidedTop && !collidedBottom && !collidedLeft && !collidedRight) {
            return "top";
        } else if (collidedBottom && !collidedTop && !collidedLeft && !collidedRight) {
            return "bottom";
        } else if (collidedLeft && !collidedRight && !collidedTop && !collidedBottom) {
            return "left";
        } else if (collidedRight && !collidedLeft && !collidedTop && !collidedBottom) {
            return "right";
        } else if (collidedTop && collidedLeft && !collidedRight && !collidedBottom) {
            return "topLeft";
        } else if (collidedTop && collidedRight && !collidedLeft && !collidedBottom) {
            return "topRight";
        } else if (collidedBottom && collidedLeft && !collidedRight && !collidedTop) {
            return "bottomLeft";
        } else if (collidedBottom && collidedRight && !collidedLeft && !collidedTop) {
            return "bottomRight";
        } else {
            return "none";
        }
    }
}
