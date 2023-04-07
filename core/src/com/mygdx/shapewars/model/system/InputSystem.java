package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.config.Role;

public abstract class InputSystem extends EntitySystem implements InputProcessor {

    protected float inputDirection;
    protected float inputValue;
    protected ImmutableArray<Entity> entities;
    protected ShapeWarsModel shapeWarsModel;

    protected boolean firingFlag;

    public InputSystem(ShapeWarsModel shapeWarsModel) {
        this.shapeWarsModel = shapeWarsModel;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(
                Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class,
                        IdentityComponent.class).get());
    }

    public void update(float deltaTime) {
        Gdx.input.setInputProcessor(this);
        if (shapeWarsModel.role == Role.Server) {
            Entity entity = entities.get(0);
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            velocityComponent.setVelocity(inputValue, inputDirection);

            if (firingFlag)
               FiringSystem.spawnBullet(entity);
        } else {
            shapeWarsModel.clientConnector.sendInputRequest(shapeWarsModel.gameModel.deviceId, inputValue, inputDirection, firingFlag);
        }
        firingFlag = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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