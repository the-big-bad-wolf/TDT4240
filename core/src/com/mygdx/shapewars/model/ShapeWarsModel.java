package com.mygdx.shapewars.model;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class ShapeWarsModel {

    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;

    public SpriteBatch batch;
    public Entity tank;

    public ShapeWarsModel() {
        batch = new SpriteBatch();
        tank = new Entity();
        tank.add(new PositionComponent(TANK_WIDTH / 2, TANK_HEIGHT / 2));
        tank.add(new VelocityComponent(0, 0));
        tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT));
        
    }
}
