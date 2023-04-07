package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class UpdateSystemClient extends EntitySystem {
    private static volatile UpdateSystemClient instance;
    protected ImmutableArray<Entity> entities;
    protected ShapeWarsModel shapeWarsModel;

    public VelocityComponent[] velocityComponents;
    public PositionComponent[] positionComponents;
    public HealthComponent[] healthComponents;
    public boolean updated; // records if the system has updated all entities and can receive new data
    // works like a thread lock

    public UpdateSystemClient(ShapeWarsModel shapeWarsModel) {
        this.shapeWarsModel = shapeWarsModel;
    }

    public static UpdateSystemClient getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new UpdateSystemClient(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class,
                        IdentityComponent.class).get());
    }

    public void update(float deltaTime) {
        if (positionComponents == null || velocityComponents == null || healthComponents == null)
            return;
        int numberOfEntitiesServer = positionComponents.length;
        int numberOfEntitiesClient = shapeWarsModel.engine.getEntities().size();
        int diff = numberOfEntitiesServer - numberOfEntitiesClient;

        if (diff > 0) {
            // server has more entities -> client needs to create #diff bullets
            for (int i = 0; i < diff; i++) {
                Entity bullet = new Entity();
                bullet.add(new PositionComponent(0, 0));
                bullet.add(new VelocityComponent(0, 0));
                bullet.add(new SpriteComponent("tank_graphics.png", 10, 10)); // todo why does a bullet have an image file??
                bullet.add(new HealthComponent(3));
                shapeWarsModel.engine.addEntity(bullet);
            }
        } else if (diff < 0) {
            // client has more entities -> client needs to delete #diff bullets
            for (int i = 0; i < diff; i++) {
                Entity e = shapeWarsModel.engine.getEntities().get(numberOfEntitiesClient - diff - 1);
                shapeWarsModel.engine.removeEntity(e);
            }
        }

        for (int i = 0; i < shapeWarsModel.engine.getEntities().size() &&
                i < positionComponents.length && i < healthComponents.length && i < velocityComponents.length; i++) {
            Entity entity = shapeWarsModel.engine.getEntities().get(i);

            // update position
            PositionComponent[] positionComponents = this.positionComponents;
            float x = positionComponents[i].getPosition().x;
            float y = positionComponents[i].getPosition().y;
            PositionComponent positionComponent = ComponentMappers.position.get(entity);
            positionComponent.setPosition(x, y);

            // update velocity
            VelocityComponent[] velocityComponents = this.velocityComponents;
            float direction = velocityComponents[i].getDirection();
            float magnitude = velocityComponents[i].getValue();
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            velocityComponent.setVelocity(magnitude, direction);

            // update health
            HealthComponent[] healthComponents = this.healthComponents;
            int health = healthComponents[i].getHealth();
            HealthComponent healthComponent = ComponentMappers.health.get(entity);
            healthComponent.setHealth(health);
        }
        updated = false;
    }
}