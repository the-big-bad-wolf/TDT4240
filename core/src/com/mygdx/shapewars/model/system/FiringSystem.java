package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.CANNON_BALL;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class FiringSystem extends EntitySystem {
  
  private static volatile FiringSystem instance;

  private FiringSystem() {};

  public static void spawnBullet(Entity entity) {
    SpriteComponent spriteComponent = ComponentMappers.sprite.get(entity);
    PositionComponent positionComponent = ComponentMappers.position.get(entity);
    VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
    Entity bullet = new Entity();
    int distanceFromShip = 75;
    float rotation = (float) Math.toRadians(spriteComponent.getSprite().getRotation());
    float x = (float) (positionComponent.getPosition().x + (spriteComponent.getSprite().getWidth() / 2)
        + (distanceFromShip * Math.cos(rotation)));
    float y = (float) (positionComponent.getPosition().y + (spriteComponent.getSprite().getHeight() / 2)
        + (distanceFromShip * Math.sin(rotation)));
    bullet.add(new PositionComponent(x, y));
    bullet.add(new VelocityComponent(10, velocityComponent.getDirection()));
    bullet.add(new SpriteComponent(CANNON_BALL, 10, 10)); // todo why does a bullet have an image file??
    bullet.add(new HealthComponent(3));
    ShapeWarsModel.addToEngine(bullet);
  }

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
}
