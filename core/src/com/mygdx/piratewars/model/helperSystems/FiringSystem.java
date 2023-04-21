package com.mygdx.piratewars.model.helperSystems;

import static com.mygdx.piratewars.config.GameConfig.CANNON_BALL;
import static com.mygdx.piratewars.config.GameConfig.MAX_BULLET_HEALTH;

import com.badlogic.ashley.core.Entity;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.ParentComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;

public abstract class FiringSystem {

    public static void spawnBullet(Entity entity) {
        SpriteComponent spriteComponent = ComponentMappers.sprite.get(entity);
        PositionComponent positionComponent = ComponentMappers.position.get(entity);
        VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
        Entity bullet = new Entity();
        float x = (float) (positionComponent.getPosition().x + (spriteComponent.getSprite().getWidth() / 2));
        float y = (float) (positionComponent.getPosition().y + (spriteComponent.getSprite().getHeight() / 2));
        bullet.add(new PositionComponent(x, y));
        bullet.add(new VelocityComponent(10, velocityComponent.getDirectionGun()));
        bullet.add(new SpriteComponent(CANNON_BALL, 10, 10));
        bullet.add(new HealthComponent(MAX_BULLET_HEALTH));
        bullet.add(new ParentComponent(entity));
        PirateWarsModel.addToEngine(bullet);
    }
}
