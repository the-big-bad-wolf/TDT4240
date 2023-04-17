package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.ENEMY_DAMAGE_ONE;
import static com.mygdx.shapewars.config.GameConfig.ENEMY_DAMAGE_TWO;
import static com.mygdx.shapewars.config.GameConfig.ENEMY_DEAD;
import static com.mygdx.shapewars.config.GameConfig.ENEMY_FULL_HEALTH;
import static com.mygdx.shapewars.config.GameConfig.PLAYER_DAMAGE_ONE;
import static com.mygdx.shapewars.config.GameConfig.PLAYER_DAMAGE_TWO;
import static com.mygdx.shapewars.config.GameConfig.PLAYER_DEAD;
import static com.mygdx.shapewars.config.GameConfig.PLAYER_FULL_HEALTH;
import static com.mygdx.shapewars.config.GameConfig.SHIP_HEIGHT;
import static com.mygdx.shapewars.config.GameConfig.SHIP_WIDTH;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class SpriteSystem extends PirateWarsSystem {

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> ships;
    private ShapeWarsModel shapeWarsModel;

    private static volatile SpriteSystem instance;

    public void addedToEngine(Engine engine) {
        entities = engine.getEntities();
        ships = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class,
                        IdentityComponent.class).get());
    }

    private SpriteSystem(ShapeWarsModel shapeWarsModel) {
        this.shapeWarsModel = shapeWarsModel;
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < ships.size(); i++) {
            Entity ship = ships.get(i);
            HealthComponent shipHealthComponent = ComponentMappers.health.get(ship);
            IdentityComponent shipIdentityComponent = ComponentMappers.identity.get(ship);
            SpriteComponent shipSpriteComponent = ComponentMappers.sprite.get(ship);

            if (shipIdentityComponent.getId() == shapeWarsModel.shipId) {
                String expectedSkin;
                if (shipHealthComponent.getHealth() >= 100) {
                    expectedSkin = PLAYER_FULL_HEALTH;
                } else if (shipHealthComponent.getHealth() >= 60) {
                    expectedSkin = PLAYER_DAMAGE_ONE;
                } else if (shipHealthComponent.getHealth() >= 20) {
                    expectedSkin = PLAYER_DAMAGE_TWO;
                } else {
                    expectedSkin = PLAYER_DEAD;
                }
                if (!(shipSpriteComponent.getSprite().getTexture().toString() == expectedSkin)) {
                    ship.remove(SpriteComponent.class);
                    ship.add(new SpriteComponent(expectedSkin, SHIP_WIDTH, SHIP_HEIGHT));
                }

                Vector2 position = ComponentMappers.position.get(ship).getPosition();
                shapeWarsModel.aimHelp.setPosition(position.x + SHIP_WIDTH / 2 + 100, position.y + SHIP_HEIGHT / 2);
            } else {
                String expectedSkin;
                if (shipHealthComponent.getHealth() >= 100) {
                    expectedSkin = ENEMY_FULL_HEALTH;
                } else if (shipHealthComponent.getHealth() >= 60) {
                    expectedSkin = ENEMY_DAMAGE_ONE;
                } else if (shipHealthComponent.getHealth() >= 20) {
                    expectedSkin = ENEMY_DAMAGE_TWO;
                } else {
                    expectedSkin = ENEMY_DEAD;
                }
                if (!(shipSpriteComponent.getSprite().getTexture().toString() == expectedSkin)) {
                    ship.remove(SpriteComponent.class);
                    ship.add(new SpriteComponent(expectedSkin, SHIP_WIDTH, SHIP_HEIGHT));
                }
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            Vector2 position = ComponentMappers.position.get(entity).getPosition();
            ComponentMappers.sprite.get(entity).getSprite().setPosition(position.x, position.y);

            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            float direction = velocityComponent.getDirection();
            ComponentMappers.sprite.get(entity).getSprite().setRotation(direction);
        }
    }

    public static SpriteSystem getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (SpriteSystem.class) {
                if (instance == null) {
                    instance = new SpriteSystem(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }
}
