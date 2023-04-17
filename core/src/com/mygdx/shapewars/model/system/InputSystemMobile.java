package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.controller.Firebutton;

public class InputSystemMobile extends InputSystem {
    private Joystick joystickShip;
    private Joystick joystickGun;
    private Firebutton firebutton;
    private FitViewport fitViewport;
    private final int outerCircleRadius;
    private boolean movingJoystickShip;
    private boolean movingJoystickGun;
    private static volatile InputSystemMobile instance;

    private InputSystemMobile(ShapeWarsModel shapeWarsModel) {
        super(shapeWarsModel);
        this.joystickShip = shapeWarsModel.joystickShip;
        this.joystickGun = shapeWarsModel.joystickGun;
        this.firebutton = shapeWarsModel.firebutton;
        this.fitViewport = shapeWarsModel.shapeWarsViewport;
        this.outerCircleRadius = Math.round(joystickShip.getOuterCircle().radius);
    }

    public static InputSystemMobile getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (InputSystemMobile.class) {
                if (instance == null) {
                    instance = new InputSystemMobile(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldCoordinates = new Vector2(screenX, screenY);
        fitViewport.unproject(worldCoordinates);

        movingJoystickShip = isCircleTouched(joystickShip.getOuterCircle(), screenX, screenY);
        movingJoystickGun = isCircleTouched(joystickGun.getOuterCircle(), screenX, screenY);
        if (isCircleTouched(firebutton.getOuterCircle(), screenX, screenY))
            firingFlag = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // check which joystick the finger was lifted off of
        if (screenX < Gdx.graphics.getWidth() / 2) {
            joystickShip.getInnerCircle().setPosition(joystickShip.getOuterCircle().x, joystickShip.getOuterCircle().y);
            inputValue = 0;
            movingJoystickShip = false;
        } else {
            joystickGun.getInnerCircle().setPosition(joystickGun.getOuterCircle().x, joystickGun.getOuterCircle().y);
            movingJoystickGun = false;
        }
        return false; // standard return value
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 worldCoordinates = new Vector2(screenX, screenY);
        fitViewport.unproject(worldCoordinates);

        // todo unite code?
        if (movingJoystickShip) {
            float deltaX = worldCoordinates.x - joystickShip.getOuterCircle().x;
            float deltaY = worldCoordinates.y - joystickShip.getOuterCircle().y;
            float deltaLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            float maxRadius = joystickShip.getOuterCircle().radius;
            if (deltaLength > maxRadius) {
                deltaX = deltaX * maxRadius / deltaLength;
                deltaY = deltaY * maxRadius / deltaLength;
            }
            joystickShip.getInnerCircle().setPosition(joystickShip.getOuterCircle().x + deltaX, joystickShip.getOuterCircle().y + deltaY);
            inputDirectionShip = angle;
            inputValue = MathUtils.clamp(deltaLength, 0, outerCircleRadius) / outerCircleRadius * MAX_SPEED;
        }
        if (movingJoystickGun) {
            float deltaX = worldCoordinates.x - joystickGun.getOuterCircle().x;
            float deltaY = worldCoordinates.y - joystickGun.getOuterCircle().y;
            float deltaLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            float maxRadius = joystickGun.getOuterCircle().radius;
            if (deltaLength > maxRadius) {
                deltaX = deltaX * maxRadius / deltaLength;
                deltaY = deltaY * maxRadius / deltaLength;
            }
            joystickGun.getInnerCircle().setPosition(joystickGun.getOuterCircle().x + deltaX, joystickGun.getOuterCircle().y + deltaY);
            inputDirectionGun = angle;
        }
        return false; // standard return value
    }

    private boolean isCircleTouched(Circle circle, int screenX, int screenY) {
        Vector2 worldCoordinates = new Vector2(screenX, screenY);
        fitViewport.unproject(worldCoordinates);
        return circle.contains(worldCoordinates);
    }
}
