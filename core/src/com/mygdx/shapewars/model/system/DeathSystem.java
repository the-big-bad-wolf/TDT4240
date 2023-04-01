package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;

public class DeathSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private static volatile DeathSystem instance;

    private DeathSystem() {
    };

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(
                Family.all(HealthComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            HealthComponent healthComponent = ComponentMappers.health.get(entity);
            if (healthComponent.getHealth() <= 0) {
                ShapeWarsModel.removeFromEngine(entity);
                System.out.println("DØD");
            }
        }
    }

    public static DeathSystem getInstance() {
        if (instance == null) {
            synchronized (DeathSystem.class) {
                if (instance == null) {
                    instance = new DeathSystem();
                }
            }
        }
        return instance;
    }
}
