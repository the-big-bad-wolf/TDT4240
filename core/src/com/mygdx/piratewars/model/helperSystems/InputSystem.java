package com.mygdx.piratewars.model.helperSystems;

import static com.mygdx.piratewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.VelocityComponent;
import com.mygdx.piratewars.config.Role;

public abstract class InputSystem extends PirateWarsSystem implements InputProcessor {
    protected float inputDirectionShip;
    protected float inputDirectionGun;
    protected float inputValue;
    protected ImmutableArray<Entity> entities;
    protected PirateWarsModel pirateWarsModel;

    protected boolean firingFlag;

    public InputSystem(PirateWarsModel pirateWarsModel) {
        this.pirateWarsModel = pirateWarsModel;
        this.pirateWarsModel.getMultiplexer().addProcessor(this);  // set your game input precessor as second

    }

    public void addedToEngine(Engine engine) {
        entities = pirateWarsModel.getEngine().getEntitiesFor(SHIP_FAMILY);
    }

    public void update(float deltaTime) {
        Gdx.input.setInputProcessor(this.pirateWarsModel.getMultiplexer()); // set multiplexer as input processor

        // todo can this be coded simpler?
        pirateWarsModel.getAimHelp().setRotation(inputDirectionGun);

        if (pirateWarsModel.getRole() == Role.Server) {
            try {
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = entities.get(i);
                    if (ComponentMappers.identity.get(entity).getId() == 0) {
                        if (ComponentMappers.health.get(entity).isDead()) {
                            inputValue = 0;
                            inputDirectionShip = ComponentMappers.velocity.get(entity).getDirection();
                            inputDirectionGun = 0;
                            firingFlag = false;
                        }
                        VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
                        velocityComponent.setVelocity(inputValue, inputDirectionShip, inputDirectionGun);
                        if (firingFlag) {
                            FiringSystem.spawnBullet(entity);
                        }
                    } 
                }
            } catch (NullPointerException e) { }
        } else {
            if (!ComponentMappers.health.get(
                entities.get(pirateWarsModel.getShipId())).isDead()) {
                        pirateWarsModel.getConnectorStrategy().sendInputRequest(pirateWarsModel.getGameModel().deviceId, inputValue, inputDirectionShip, inputDirectionGun, firingFlag);
                    } else {
                        pirateWarsModel.getConnectorStrategy().sendInputRequest(pirateWarsModel.getGameModel().deviceId, 0, ComponentMappers.velocity.get(entities.get(pirateWarsModel.getShipId())).getDirection(), 0, false);
                    }
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