package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.types.Role;
import com.mygdx.shapewars.network.client.ClientConnector;

public abstract class InputSystem extends EntitySystem implements InputProcessor {

    protected float inputDirection;
    protected float inputValue;
    private ImmutableArray<Entity> entities;
    private Role role;
    private ClientConnector clientConnector;
    private String clientId; // find a way to remove all of these fields

    public boolean usedJoystick = false; // todo remove

    public InputSystem(Role role, ClientConnector clientConnector, String clientId) {
        this.role = role;
        this.clientConnector = clientConnector;
        this.clientId = clientId;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class,
                        IdentityComponent.class).get());
    }

    public void update(float deltaTime) {
        Gdx.input.setInputProcessor(this);
        if (role == Role.Server) {
            Entity entity = entities.get(0);
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            if (usedJoystick) {
                velocityComponent.setVelocityJoystick(inputValue, inputDirection);
            } else {
                velocityComponent.setVelocity(inputValue, velocityComponent.getDirection() + inputDirection);
            }
            // if (space) {
            //    FiringSystem.spawnBullet(entity);
            //    space = false;
            //}
            // velocityComponent.setMagnitudeAndDirection(inputValue, inputDirection);
        } else {
            clientConnector.sendInput(clientId, inputValue, inputDirection); // update clientId
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("i should not be called");
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("i should not be called");
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}