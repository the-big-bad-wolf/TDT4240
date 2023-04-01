package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class FiringSystem extends EntitySystem {
  private ImmutableArray<Entity> entities;
  
  private static volatile FiringSystem instance;

  private FiringSystem() {};

  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class).get());
  }

  public void update(float deltaTime) {


    for (Entity entity : entities) {
      


    }
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