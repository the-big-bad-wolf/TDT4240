package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;
import static com.mygdx.shapewars.config.GameConfig.MAX_TURN_RATE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.config.Role;

public class InputSystemDesktop extends InputSystem {
    private static volatile InputSystemDesktop instance;

    private InputSystemDesktop(Role role, ClientConnector clientConnector, String clientId) {
        super(role, clientConnector, clientId);
    }

    public static InputSystemDesktop getInstance(Role role, ClientConnector clientConnector, String clientId) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new InputSystemDesktop(role, clientConnector, clientId);
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
        boolean left = isKeyPressed(Input.Keys.A) || isKeyPressed(Input.Keys.LEFT);
        boolean right = isKeyPressed(Input.Keys.D) || isKeyPressed(Input.Keys.RIGHT);

        if (left && right)
            return 0;
        else if (left)
            return MAX_TURN_RATE;
        else if (right)
            return  -MAX_TURN_RATE;
        return 0;
    }

    @Override
    public boolean keyDown(int keycode) {
        // keyDown is only used for firing as spamming bullets should not be allowed
        if (keycode == Input.Keys.SPACE)
            firing = true;

        return false; // standard return value
    }

    /*
    * todo: implement the shooting on mouse click and point the gun to the mouse pointer
    */
}
