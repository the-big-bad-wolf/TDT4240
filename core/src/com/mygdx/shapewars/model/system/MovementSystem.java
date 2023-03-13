package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class MovementSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

  private static volatile MovementSystem instance;

	private MovementSystem() {}

	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class).get());
	}

	public void update(float deltaTime) {
		for (Entity entity : entities) {
			PositionComponent position = ComponentMappers.position.get(entity);
			VelocityComponent velocity = ComponentMappers.velocity.get(entity);
			SpriteComponent sprite = ComponentMappers.sprite.get(entity);


			// calculate and set position
			float radians = MathUtils.degreesToRadians * velocity.getDirection();

			float newX = position.getPosition().x + MathUtils.cos(radians) * velocity.getValue();
			float newY = position.getPosition().y + MathUtils.sin(radians) * velocity.getValue();

			position.addPosition(newX, newY);

			// set direction
			sprite.getSprite().setRotation(velocity.getDirection());

			sprite.getSprite().setPosition(position.getPosition().x, position.getPosition().y);
		}
	}
	public static MovementSystem getInstance() {
		if (instance == null) {
			synchronized (MovementSystem.class) {
				if (instance == null) {
					instance = new MovementSystem();
				}
			}
		}
		return instance;
	}
}
