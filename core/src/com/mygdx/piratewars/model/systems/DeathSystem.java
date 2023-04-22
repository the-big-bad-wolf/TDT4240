package com.mygdx.piratewars.model.systems;

import static com.mygdx.piratewars.config.GameConfig.SHIP_DEAD;
import static com.mygdx.piratewars.config.GameConfig.SHIP_WIDTH;
import static com.mygdx.piratewars.config.GameConfig.SHIP_HEIGHT;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.helperSystems.PirateWarsSystem;

public class DeathSystem extends PirateWarsSystem {
    private ImmutableArray<Entity> entities;
    private PirateWarsModel model;

    private static volatile DeathSystem instance;

    private DeathSystem(PirateWarsModel model) {
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
                || position.x < -1000 || position.x > Gdx.graphics.getWidth() + 1000
                || position.y < -1000 || position.y > Gdx.graphics.getHeight() + 1000) {
                if (entity.getComponent(IdentityComponent.class) != null) {
                    SpriteComponent deadShipSprite = new SpriteComponent(SHIP_DEAD, SHIP_WIDTH, SHIP_HEIGHT);
                    deadShipSprite.getSprite().setPosition(position.x, position.y);
                    deadShipSprite.setRotation(ComponentMappers.velocity.get(entity).getDirection());
                    entity.remove(SpriteComponent.class);
                    entity.add(deadShipSprite);
                    entity.remove(HealthComponent.class);
                    model.numPlayersAlive -= 1;
                } else {
                    model.engine.removeEntity(entity);
                }
            }
        }
    }

    public static DeathSystem getInstance(PirateWarsModel model) {
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
