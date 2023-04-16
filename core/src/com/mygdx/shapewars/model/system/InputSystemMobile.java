package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.controller.Firebutton;

public class InputSystemMobile extends InputSystem {
    private Joystick joystick;
    private Firebutton firebutton;
    private FitViewport fitViewport;
    private final int outerCircleRadius;
    private boolean movingJoystick;
    private static volatile InputSystemMobile instance;

    private InputSystemMobile(ShapeWarsModel shapeWarsModel) {
        super(shapeWarsModel);
        this.joystick = shapeWarsModel.joystick;
        this.firebutton = shapeWarsModel.firebutton;
        this.fitViewport = shapeWarsModel.shapeWarsViewport;
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
        Vector2 worldCoordinates = new Vector2(screenX, screenY);
        fitViewport.unproject(worldCoordinates);
        movingJoystick = joystick.getOuterCircle().contains(worldCoordinates);
        if (firebutton.getOuterCircle().contains(worldCoordinates))
            firingFlag = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x, joystick.getOuterCircle().y);
        inputValue = 0;
        movingJoystick = false;
        return false; // standard return value
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 worldCoordinates = new Vector2(screenX, screenY);
        fitViewport.unproject(worldCoordinates);

        if (movingJoystick) {
            float deltaX = worldCoordinates.x - joystick.getOuterCircle().x;
            float deltaY = worldCoordinates.y - joystick.getOuterCircle().y;
            float deltaLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            float maxRadius = joystick.getOuterCircle().radius;
            if (deltaLength > maxRadius) {
                deltaX = deltaX * maxRadius / deltaLength;
                deltaY = deltaY * maxRadius / deltaLength;
            }
            joystick.getInnerCircle().setPosition(joystick.getOuterCircle().x + deltaX, joystick.getOuterCircle().y + deltaY);
            inputDirectionShip = angle;
            inputValue = MathUtils.clamp(deltaLength, 0, outerCircleRadius) / outerCircleRadius * MAX_SPEED;
        }
        return false; // standard return value
    }

    /*
     * todo: add firing button
     */
}
