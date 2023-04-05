package com.mygdx.shapewars.model.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.types.Role;

public class InputSystemMobile extends InputSystem {
    private Joystick joystick;
    private final int outerCircleRadius;
    private boolean movingJoystick;
    private static volatile InputSystemMobile instance;

    private InputSystemMobile(Role role, ClientConnector clientConnector, String clientId, Joystick joystick) {
        super(role, clientConnector, clientId);
        this.joystick = joystick;
        this.outerCircleRadius = Math.round(joystick.getOuterCircle().radius);
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
        movingJoystick = joystick.getOuterCircle().contains(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x, joystick.getOuterCircle().y);
        inputValue = 0;
        inputDirection = 0;
        movingJoystick = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!movingJoystick)
            return false;

        float deltaX = screenX - 275 - joystick.getOuterCircle().x; // todo sophie why did i need the 275 here? something with the map borders?
        float deltaY = Gdx.graphics.getHeight() - screenY - joystick.getOuterCircle().y;

        deltaX = MathUtils.clamp(deltaX, -outerCircleRadius, outerCircleRadius);
        deltaY = MathUtils.clamp(deltaY, -outerCircleRadius, outerCircleRadius);

        joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x + deltaX, joystick.getOuterCircle().y + deltaY);

        float maxSpeed = 5.0f;
        float maxTurnRate = 2.0f;
        inputDirection = -deltaX / outerCircleRadius * maxTurnRate;
        inputValue = deltaY / outerCircleRadius * maxSpeed;
        return false;
    }

    /*
     * todo: decide on joystick implementation, add firing button
     */
}
