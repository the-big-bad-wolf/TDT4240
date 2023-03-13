package com.mygdx.shapewars.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.model.system.MovementSystem;

public class ShapeWarsModel {

    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;

    public SpriteBatch batch;
    public Entity tank;
    public Engine engine;
    public MovementSystem movementSystem;
    private TiledMap map;


    public ShapeWarsModel() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/secondMap.tmx");
        batch = new SpriteBatch();
        engine = new Engine();
        tank = new Entity();
        movementSystem = movementSystem.getInstance(map);
        tank.add(new PositionComponent(TANK_WIDTH / 2, TANK_HEIGHT / 2));
        tank.add(new VelocityComponent(0, 0));
        tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT));
        tank.add(new HealthComponent());
        engine.addEntity(tank);
        engine.addSystem(movementSystem);
    }

    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public TiledMap getMap() {
        return map;
    }
}
