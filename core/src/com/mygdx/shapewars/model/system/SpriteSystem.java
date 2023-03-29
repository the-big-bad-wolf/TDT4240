package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.Role;
import com.mygdx.shapewars.network.client.ClientConnector;

public class SpriteSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private static volatile SpriteSystem instance;

    public void addedToEngine(Engine engine) {
        entities = engine.getEntities();
    }

    private SpriteSystem() {
    }

    @Override
    public void update(float deltaTime) {
        // todo sprite system
        for (Entity entity : entities) {
            Vector2 position = ComponentMappers.position.get(entity).getPosition();
            ComponentMappers.sprite.get(entity).getSprite().setPosition(position.x, position.y);

            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            float direction = velocityComponent.getDirection();
            ComponentMappers.sprite.get(entity).getSprite().setRotation(direction);
        }

    }

    public static SpriteSystem getInstance() {
        if (instance == null) {
            synchronized (SpriteSystem.class) {
                if (instance == null) {
                    instance = new SpriteSystem();
                }
            }
        }
        return instance;
    }
}
