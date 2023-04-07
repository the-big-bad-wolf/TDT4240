package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;
import static com.mygdx.shapewars.config.GameConfig.MAX_TURN_RATE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.model.ShapeWarsModel;


public class InputSystemMobile extends InputSystem {
    private Joystick joystick;
    private final int outerCircleRadius;
    private boolean movingJoystick;
    private static volatile InputSystemMobile instance;

    private InputSystemMobile(ShapeWarsModel shapeWarsModel) {
        super(shapeWarsModel);
        this.joystick = shapeWarsModel.joystick;
        this.outerCircleRadius = Math.round(joystick.getOuterCircle().radius);
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

        inputDirection = -deltaX / outerCircleRadius * MAX_TURN_RATE;
        inputValue = deltaY / outerCircleRadius * MAX_SPEED;
        return false;
    }

    /*
     * todo: decide on joystick implementation, add firing button
     */
}
