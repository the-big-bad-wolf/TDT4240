package com.mygdx.piratewars.model.systems;

import static com.mygdx.piratewars.config.GameConfig.MAX_BULLET_HEALTH;
import static com.mygdx.piratewars.config.GameConfig.BULLET_FAMILY;
import static com.mygdx.piratewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.ParentComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.helperSystems.CollisionSystem;
import com.mygdx.piratewars.model.helperSystems.PirateWarsSystem;

public class DamageSystem extends PirateWarsSystem {

    private ImmutableArray<Entity> bullets;
    private ImmutableArray<Entity> ships;
    private static volatile DamageSystem instance;

    private DamageSystem() {
    }

    public void addedToEngine(Engine engine) {
        bullets = engine.getEntitiesFor(BULLET_FAMILY);
        ships = engine.getEntitiesFor(SHIP_FAMILY);
    }

    public void update(float deltaTime) {
        for (int i = 0; i < bullets.size(); i++) {
            Entity bullet = bullets.get(i);
            PositionComponent bulletPositionComponent = ComponentMappers.position.get(bullet);
            HealthComponent bulletHealthComponent = ComponentMappers.health.get(bullet);
            SpriteComponent bulletSpriteComponent = ComponentMappers.sprite.get(bullet);

            // Check if bullet hits ship
            for (Entity ship : ships) {
                IdentityComponent shipIdentityComponent = ComponentMappers.identity.get(ship);
                PositionComponent shipPositionComponent = ComponentMappers.position.get(ship);
                SpriteComponent shipSpriteComponent = ComponentMappers.sprite.get(ship);
                HealthComponent shipHealthComponent = ComponentMappers.health.get(ship);
                if (shipHealthComponent.isDead()) {
                    continue;
                }
                if (CollisionSystem.checkCollisionWithEntity(bulletPositionComponent, bulletSpriteComponent,
                        shipPositionComponent, shipSpriteComponent)) {
                    ParentComponent bulletParentComponent = ComponentMappers.parent.get(bullet);
                    if (shipIdentityComponent.getId() == (bulletParentComponent.getParent().getComponent(IdentityComponent.class).getId())) {
                        if (bulletHealthComponent.getHealth() == MAX_BULLET_HEALTH) {
                            break;
                        }
                    }
                    shipHealthComponent.takeDamage(40);
                    bulletHealthComponent.takeDamage(MAX_BULLET_HEALTH);
                    break;
                }
            }

            // Check if bullet hits bullet
            for (int j = i + 1; j < bullets.size(); j++) {
                Entity otherBullet = bullets.get(j);
                PositionComponent otherBulletPositionComponent = ComponentMappers.position.get(otherBullet);
                SpriteComponent otherBulletSpriteComponent = ComponentMappers.sprite.get(otherBullet);
                if (CollisionSystem.checkCollisionWithEntity(bulletPositionComponent, bulletSpriteComponent,
                        otherBulletPositionComponent,
                        otherBulletSpriteComponent)) {
                    HealthComponent otherBulletHealthComponent = ComponentMappers.health.get(otherBullet);
                    bulletHealthComponent.takeDamage(MAX_BULLET_HEALTH);
                    otherBulletHealthComponent.takeDamage(MAX_BULLET_HEALTH);
                    break;
                }
            }
        }
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

    public void dispose() {
        instance = null;
    }
}
