package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.Role;
import com.mygdx.shapewars.network.client.ClientConnector;
import java.util.UUID;

public class InputSystem extends EntitySystem implements InputProcessor {
    private ImmutableArray<Entity> entities;
    private Joystick joystick;
    private float inputDirection;
    private float inputValue;
    private boolean usedJoystick;


    private VelocityComponent velocityComponent;
    private PositionComponent positionComponent;
    private IdentityComponent identityComponent;
    private SpriteComponent spriteComponent;
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean space;
    boolean driving;
    boolean movingThumbstick;

    private Role role;
    private ClientConnector clientConnector;
    private String clientId; // find a way to remove all of these fields

    private static volatile InputSystem instance;

    private InputSystem(Role role, ClientConnector clientConnector, String clientId, Joystick joystick) {
        this.role = role;
        this.clientConnector = clientConnector;
        this.clientId = clientId;
        this.joystick = joystick;
    }

    public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class,
            IdentityComponent.class).get());
    }

    public void update(float deltaTime) {
        Gdx.input.setInputProcessor(this);

        if (left) {
            inputDirection = 2;
        } else if (right) {
            inputDirection = -2;
        } else if (!usedJoystick){
            inputDirection = 0;
        }

        if (up || driving) {
            inputValue = 5;
        } else if (down) {
            inputValue = -5;
        }
        else {
            inputValue = 0;
        }

        // todo completely change
        if (role == Role.Server) {
            Entity entity = entities.get(0);
            VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);
            if (usedJoystick) {
                velocityComponent.setVelocityJoystick(inputValue, inputDirection);
            }
            else {
                velocityComponent.setVelocity(inputValue, velocityComponent.getDirection() + inputDirection);
            }
            if (space) {
                FiringSystem.spawnBullet(entity);
                space = false;
            }
        } else {
            clientConnector.sendInput(clientId, inputValue, inputDirection); // update clientId
        }

    }

    public static InputSystem getInstance(Role role, ClientConnector clientConnector, String clientId, Joystick joystick) {
        if (instance == null) {
            synchronized (InputSystem.class) {
                if (instance == null) {
                    instance = new InputSystem(role, clientConnector, clientId, joystick);
                }
            }
        }
        return instance;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            left = true;
        } else if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            right = true;
        }
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            up = true;
        } else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            down = true;
        }
        usedJoystick = false;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            left = false;
        } else if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            right = false;
        }
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            up = false;
        } else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
            down = false;
        }
        if (keycode == Input.Keys.SPACE) {
            space = true;
        }
        usedJoystick = false;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (joystick.getOuterCircle().contains(screenX, Gdx.graphics.getHeight()-screenY)) {
            movingThumbstick = true;
            joystick.getInnerCircle().setPosition(screenX, Gdx.graphics.getHeight() - screenY);
        }
        else {
            movingThumbstick = false;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x, joystick.getOuterCircle().y);
        driving = false;
        movingThumbstick = false;
        usedJoystick = false;
        inputDirection = 0;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (movingThumbstick) {
            float deltaX = screenX - joystick.getOuterCircle().x;
            float deltaY = Gdx.graphics.getHeight() - screenY - joystick.getOuterCircle().y;
            float deltaLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            float maxRadius = joystick.getOuterCircle().radius;
            if (deltaLength > maxRadius) {
                deltaX = deltaX * maxRadius / deltaLength;
                deltaY = deltaY * maxRadius / deltaLength;
            }
            //inputThumbstick.getInnerCircleSprite().setCenter(inputThumbstick.getOuterCircle().x + deltaX, Gdx.graphics.getHeight() - (inputThumbstick.getOuterCircle().y - deltaY)); // Bewegen des inneren Kreises an die neue Position
            joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x + deltaX, (joystick.getOuterCircle().y + deltaY));

            deltaX = screenX - joystick.getOuterCircle().x;
            deltaY = (Gdx.graphics.getHeight() - screenY) - joystick.getOuterCircle().y;
            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            inputDirection = angle;

            if (joystick.getOuterCircle().x != joystick.getInnerCircle().x && joystick.getOuterCircle().y != joystick.getInnerCircle().y) {
                driving = true;
            }
            usedJoystick = true;
        }
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
