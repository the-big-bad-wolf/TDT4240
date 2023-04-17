package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.config.Role;

public abstract class InputSystem extends EntitySystem implements InputProcessor {
    protected float inputDirectionShip;
    protected float inputDirectionGun;
    protected float inputValue;
    protected ImmutableArray<Entity> entities;
    protected ShapeWarsModel shapeWarsModel;

    protected boolean firingFlag;

    public InputSystem(ShapeWarsModel shapeWarsModel) {
        this.shapeWarsModel = shapeWarsModel;
        this.shapeWarsModel.multiplexer.addProcessor(this);  // set your game input precessor as second

    }

    public void addedToEngine(Engine engine) { }

    public void update(float deltaTime) {
        entities = shapeWarsModel.engine.getEntitiesFor(SHIP_FAMILY);
        Gdx.input.setInputProcessor(this.shapeWarsModel.multiplexer); // set multiplexer as input processor

        // todo can this be coded simpler?
        Gdx.input.setInputProcessor(this);
        shapeWarsModel.aimHelp.setRotation(inputDirectionGun);

        if (shapeWarsModel.role == Role.Server) {
            try {
                Entity entity = null;
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    if (ComponentMappers.identity.get(e).getId() == 0) {
                        entity = e;
                    }
                    VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
                    velocityComponent.setVelocity(inputValue, inputDirectionShip, inputDirectionGun);
                    if (firingFlag)
                        FiringSystem.spawnBullet(entity);
                }
            } catch (NullPointerException e) { }
        } else {
            shapeWarsModel.clientConnector.sendInputRequest(shapeWarsModel.gameModel.deviceId, inputValue, inputDirectionShip, inputDirectionGun, firingFlag);
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