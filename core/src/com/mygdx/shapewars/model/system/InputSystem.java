package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.Role;
import com.mygdx.shapewars.network.client.ClientConnector;

import java.util.UUID;

public class InputSystem extends EntitySystem {
  private ImmutableArray<Entity> entities;

  private Role role;
  private ClientConnector clientConnector;
  private String clientId; // find a way to remove all of these fields

  private static volatile InputSystem instance;

  private InputSystem(Role role, ClientConnector clientConnector, String clientId) {
      this.role = role;
      this.clientConnector = clientConnector;
      this.clientId = clientId;
  };

  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class).get());
  }

  public void update(float deltaTime) {
        int inputDirection = 0, inputValue = 0;

        // todo add arrow buttons back
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputDirection = 2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputDirection = -2;
        }

        //Controls value of velocity
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            inputValue = 5;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            inputValue = -5;
        }



        // todo completely change
        if (role == Role.Server) {
            Entity entity = entities.get(0);
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            velocityComponent.setMagnitudeAndDirection(inputValue, inputDirection);
        } else {
            clientConnector.sendInput(clientId, inputValue, inputDirection); // update clientId
        }

  }

  public static InputSystem getInstance(Role role, ClientConnector clientConnector, String clientId) {
		if (instance == null) {
			synchronized (InputSystem.class) {
				if (instance == null) {
					instance = new InputSystem(role, clientConnector, clientId);
				}
			}
		}
		return instance;
	}
}
