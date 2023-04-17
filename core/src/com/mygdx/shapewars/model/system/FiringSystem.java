package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.CANNON_BALL;
import static com.mygdx.shapewars.config.GameConfig.MAX_BULLET_HEALTH;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.ParentComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class FiringSystem extends PirateWarsSystem {
  
  private static volatile FiringSystem instance;

  private FiringSystem() {};

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
    ShapeWarsModel.addToEngine(bullet);
  }

  // todo make this abstract
  public static FiringSystem getInstance() {
		if (instance == null) {
			synchronized (FiringSystem.class) {
				if (instance == null) {
					instance = new FiringSystem();
				}
			}
		}
		return instance;
	}

    public void dispose() {
        instance = null;
    }
}
