package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class CollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private static volatile CollisionSystem instance;

    private CollisionSystem() {};

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class).get());
    }

    public void update() {

    }

    public static CollisionSystem getInstance() {
        if (instance == null) {
            synchronized (CollisionSystem.class) {
                if (instance == null) {
                    instance = new CollisionSystem();
                }
            }
        }
        return instance;
    }
}
