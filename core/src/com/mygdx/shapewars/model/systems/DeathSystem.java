package com.mygdx.shapewars.model.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.helperSystems.PirateWarsSystem;

public class DeathSystem extends PirateWarsSystem {
    private ImmutableArray<Entity> entities;
    private ShapeWarsModel model;

    private static volatile DeathSystem instance;

    private DeathSystem(ShapeWarsModel model) {
        this.model = model;
    };

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(
                Family.all(HealthComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            HealthComponent healthComponent = ComponentMappers.health.get(entity);
            Vector2 position = ComponentMappers.position.get(entity).getPosition();
            if (healthComponent.getHealth() <= 0 
                || position.x < -100 || position.x > Gdx.graphics.getWidth() + 100
                || position.y < -100 || position.y > Gdx.graphics.getHeight() + 100) {
                model.engine.removeEntity(entity);
            }
        }
    }

    public static DeathSystem getInstance(ShapeWarsModel model) {
        if (instance == null) {
            synchronized (DeathSystem.class) {
                if (instance == null) {
                    instance = new DeathSystem(model);
                }
            }
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }
}
