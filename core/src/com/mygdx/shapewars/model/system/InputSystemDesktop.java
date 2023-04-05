package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;
import static com.mygdx.shapewars.config.GameConfig.MAX_TURN_RATE;

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
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
            this.inputDirection = MAX_TURN_RATE;
        else if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
            this.inputDirection = -MAX_TURN_RATE;

        if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
            inputValue = MAX_SPEED;
        else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
            inputValue = -MAX_SPEED;

        if (keycode == Input.Keys.SPACE)
            firing = true;

        return false; // standard return value
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT || keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
            this.inputDirection = 0;

        if (keycode == Input.Keys.W || keycode == Input.Keys.UP || keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
            this.inputValue = 0;

        return false; // standard return value
    }

    /*
    * todo: implement the shooting on mouse click and point the gun to the mouse pointer
    */
}
