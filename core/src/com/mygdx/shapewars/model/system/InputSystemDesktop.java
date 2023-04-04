package com.mygdx.shapewars.model.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.types.Role;

public class InputSystemDesktop extends InputSystem {
    private static volatile InputSystemDesktop instance;

    private InputSystemDesktop(Role role, ClientConnector clientConnector, String clientId) {
        super(role, clientConnector, clientId);
        Gdx.input.setInputProcessor(this);
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
            this.inputDirection = 2;
        else if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
            this.inputDirection = -2;

        if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
            inputValue = 5;
        else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
            inputValue = -5;

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
