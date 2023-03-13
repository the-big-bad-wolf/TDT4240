package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class InputSystem extends EntitySystem {
  private ImmutableArray<Entity> entities;
  private int value;
  private int direction;
  
  private static volatile InputSystem instance;

  private InputSystem() {};

  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class).get());
  }

  public void update(float deltaTime) {
    for (Entity entity : entities) {
      VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
      velocityComponent.setDirection(direction);
      velocityComponent.setValue(value);

    }
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void addDirection(int direction) {
    this.direction += direction;
  }

  public static InputSystem getInstance() {
		if (instance == null) {
			synchronized (InputSystem.class) {
				if (instance == null) {
					instance = new InputSystem();
				}
			}
		}
		return instance;
	}

}
