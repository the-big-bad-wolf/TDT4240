package com.mygdx.piratewars.model.systems;

import static com.mygdx.piratewars.config.GameConfig.ENEMY_DAMAGE_ONE;
import static com.mygdx.piratewars.config.GameConfig.ENEMY_DAMAGE_TWO;
import static com.mygdx.piratewars.config.GameConfig.ENEMY_DEAD;
import static com.mygdx.piratewars.config.GameConfig.ENEMY_FULL_HEALTH;
import static com.mygdx.piratewars.config.GameConfig.PLAYER_DAMAGE_ONE;
import static com.mygdx.piratewars.config.GameConfig.PLAYER_DAMAGE_TWO;
import static com.mygdx.piratewars.config.GameConfig.PLAYER_DEAD;
import static com.mygdx.piratewars.config.GameConfig.PLAYER_FULL_HEALTH;
import static com.mygdx.piratewars.config.GameConfig.SHIP_HEIGHT;
import static com.mygdx.piratewars.config.GameConfig.SHIP_WIDTH;
import static com.mygdx.piratewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;
import com.mygdx.piratewars.model.helperSystems.PirateWarsSystem;

public class SpriteSystem extends PirateWarsSystem {

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> ships;
    private PirateWarsModel pirateWarsModel;

    private static volatile SpriteSystem instance;

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class).get());
        ships = engine.getEntitiesFor(SHIP_FAMILY);
    }

    private SpriteSystem(PirateWarsModel pirateWarsModel) {
        this.pirateWarsModel = pirateWarsModel;
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < ships.size(); i++) {
            Entity ship = ships.get(i);
            HealthComponent shipHealthComponent = ComponentMappers.health.get(ship);
            IdentityComponent shipIdentityComponent = ComponentMappers.identity.get(ship);
            SpriteComponent shipSpriteComponent = ComponentMappers.sprite.get(ship);

            if (shipHealthComponent.isDead()) {
                continue;
            }

            if (shipIdentityComponent.getId() == pirateWarsModel.shipId) {
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
                pirateWarsModel.aimHelp.setPosition(position.x + SHIP_WIDTH / 2 + 100, position.y + SHIP_HEIGHT / 2);
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

    public static SpriteSystem getInstance(PirateWarsModel pirateWarsModel) {
        if (instance == null) {
            synchronized (SpriteSystem.class) {
                if (instance == null) {
                    instance = new SpriteSystem(pirateWarsModel);
                }
            }
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }
}
