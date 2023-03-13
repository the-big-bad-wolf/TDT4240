package com.mygdx.shapewars.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.model.system.InputSystem;
import com.mygdx.shapewars.model.system.MovementSystem;

public class ShapeWarsModel {

    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;

    public static final int NUM_PLAYERS = 2;

    public SpriteBatch batch;
    public Engine engine;
    public MovementSystem movementSystem;
    private TiledMap map;

    public InputSystem inputSystem;

    public ShapeWarsModel() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/secondMap.tmx");
        batch = new SpriteBatch();
        engine = new Engine();

        for (int i = 0; i < NUM_PLAYERS; i++) {
            Entity tank = new Entity();
            tank.add(new PositionComponent((TANK_WIDTH / 2) * (i + 1) + 200, (TANK_HEIGHT / 2) * (i + 1) + 200));
            tank.add(new VelocityComponent(0, 0));
            tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT));
            tank.add(new HealthComponent());
            tank.add(new IdentityComponent(i));
            engine.addEntity(tank);
        }
        movementSystem = movementSystem.getInstance(map);
        inputSystem = inputSystem.getInstance();
        engine.addSystem(movementSystem);
        engine.addSystem(inputSystem);
    }

    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public TiledMap getMap() {
        return map;
    }
}
