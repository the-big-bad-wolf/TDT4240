package com.mygdx.shapewars.model.system;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class DamageSystem extends EntitySystem {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> tanks;


    private static volatile DamageSystem instance;

    private DamageSystem() {}

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class)
                        .exclude(IdentityComponent.class).get());
        tanks = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class,
                        IdentityComponent.class).get());
    }

    public void update(float deltaTime) {
        for (Entity bullet : bullets) {
            PositionComponent bulletPositionComponent = ComponentMappers.position.get(bullet);
            HealthComponent bulletHealthComponent = ComponentMappers.health.get(bullet);

            // Check if bullet hits tank
            for (Entity tank : tanks) {
                PositionComponent tankPositionComponent = ComponentMappers.position.get(tank);
                SpriteComponent tankSpriteComponent = ComponentMappers.sprite.get(tank);
                HealthComponent tankHealthComponent = ComponentMappers.health.get(tank);

                if (checkCollisionWithTank(bulletPositionComponent, tankPositionComponent, tankSpriteComponent)) {
                    tankHealthComponent.takeDamage(40);
                    bulletHealthComponent.takeDamage(100);
                    break;
                }
            }
        }
    }
    private boolean checkCollisionWithTank(PositionComponent bulletPosition, PositionComponent tankPositionComponent,
                                           SpriteComponent tankSpriteComponent) {
        float x1 = tankPositionComponent.getPosition().x;
        float y1 = tankPositionComponent.getPosition().y;
        float width = tankSpriteComponent.getSprite().getWidth();
        float height = tankSpriteComponent.getSprite().getWidth();
        float x2 = bulletPosition.getPosition().x;
        float y2 = bulletPosition.getPosition().y;
        return x2 >= x1 && x2 <= x1 + width && y2 >= y1 && y2 <= y1 + height;
    }


    public static DamageSystem getInstance() {
        if (instance == null) {
            synchronized (DamageSystem.class) {
                if (instance == null) {
                    instance = new DamageSystem();
                }
            }
        }
        return instance;
    }
}
