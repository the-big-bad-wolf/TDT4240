package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;
import static com.mygdx.shapewars.config.GameConfig.MAX_TURN_RATE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;

public class InputSystemDesktop extends InputSystem {
    private static volatile InputSystemDesktop instance;

    private InputSystemDesktop(ShapeWarsModel shapeWarsModel) {
        super(shapeWarsModel);
    }

    public static InputSystemDesktop getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new InputSystemDesktop(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    @Override
    public void update(float deltaTime) {
        inputValue = getInputValue();
        inputDirection = getInputDirection();
        super.update(deltaTime);
    }

    private boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }

    private int getInputValue() {
        boolean forwards = isKeyPressed(Input.Keys.W) || isKeyPressed(Input.Keys.UP);
        boolean backwards = isKeyPressed(Input.Keys.S) || isKeyPressed(Input.Keys.DOWN);

        if (forwards && backwards)
            return 0;
        else if (forwards)
            return MAX_SPEED;
        else if (backwards)
            return  -MAX_SPEED;
        return 0;
    }

    private int getInputDirection() {
        // todo absolutely remove this. this is for when the tank the movement system belongs to has been destroyed
        int currentDir;
        try {
            currentDir = (int) ComponentMappers.velocity.get(entities.get(shapeWarsModel.tankId)).getDirection();
        } catch (Exception e) {
            return 0;
        }


        boolean left = isKeyPressed(Input.Keys.A) || isKeyPressed(Input.Keys.LEFT);
        boolean right = isKeyPressed(Input.Keys.D) || isKeyPressed(Input.Keys.RIGHT);

        if (left && right)
            return currentDir;
        else if (left)
            return MAX_TURN_RATE + currentDir;
        else if (right)
            return  -MAX_TURN_RATE + currentDir;
        return currentDir;
    }

    @Override
    public boolean keyDown(int keycode) {
        // keyDown is only used for firing as spamming bullets should not be allowed
        if (keycode == Input.Keys.SPACE)
            firingFlag = true;

        return false; // standard return value
    }

    /*
    * todo: implement the shooting on mouse click and point the gun to the mouse pointer
    */
}
