package com.mygdx.shapewars.model.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.types.Role;

public class InputSystemMobile extends InputSystem {
    private Joystick joystick;
    private boolean movingThumbstick;
    private static volatile InputSystemMobile instance;

    private InputSystemMobile(Role role, ClientConnector clientConnector, String clientId, Joystick joystick) {
        super(role, clientConnector, clientId);
        this.joystick = joystick;
        Gdx.input.setInputProcessor(this);
    }

    public static InputSystemMobile getInstance(Role role, ClientConnector clientConnector, String clientId, Joystick joystick) {
        if (instance == null) {
            synchronized (InputSystemMobile.class) {
                if (instance == null) {
                    instance = new InputSystemMobile(role, clientConnector, clientId, joystick);
                }
            }
        }
        return instance;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (joystick.getOuterCircle().contains(screenX, Gdx.graphics.getHeight() - screenY)) {
            movingThumbstick = true;
            joystick.getInnerCircle().setPosition(screenX, Gdx.graphics.getHeight() - screenY);
        }
        else {
            movingThumbstick = false;
        }

        return false; // standard return value
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x, joystick.getOuterCircle().y);
        inputValue = 0;
        movingThumbstick = false;
        // usedJoystick = false;
        // inputDirection = 0;
        return false; // standard return value
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
                inputValue = 5;
            }
            usedJoystick = true;
        }
        return false; // standard return value
    }
}
