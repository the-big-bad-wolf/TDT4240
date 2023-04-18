package com.mygdx.shapewars.model.systems;

import static com.mygdx.shapewars.config.GameConfig.MAX_SPEED;
import static com.mygdx.shapewars.config.GameConfig.MAX_TURN_RATE;
import static com.mygdx.shapewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.helperSystems.InputSystem;

public class InputSystemDesktop extends InputSystem {
    private static volatile InputSystemDesktop instance;
    private FitViewport fitViewport; // todo move to other class

    private InputSystemDesktop(ShapeWarsModel shapeWarsModel) {
        super(shapeWarsModel);
        this.fitViewport = shapeWarsModel.shapeWarsViewport;
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

    public void dispose() {
        instance = null;
    }

    @Override
    public void update(float deltaTime) {
        inputValue = getInputValue();
        inputDirectionShip = getInputDirection();
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
        entities = shapeWarsModel.engine.getEntitiesFor(SHIP_FAMILY);
        int currentDir = 0;
        try {
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                if (ComponentMappers.identity.get(e).getId() == shapeWarsModel.shipId)
                    currentDir = (int) ComponentMappers.velocity.get(e).getDirection();
            }
        } catch (Exception e) { }

        boolean left = isKeyPressed(Input.Keys.A) || isKeyPressed(Input.Keys.LEFT);
        boolean right = isKeyPressed(Input.Keys.D) || isKeyPressed(Input.Keys.RIGHT);

        if (left && right)
            return currentDir;
        else if (left)
            return MAX_TURN_RATE + currentDir;
        else if (right)
            return  -MAX_TURN_RATE + currentDir;
        return currentDir % 360;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Buttons.LEFT)
            firingFlag = true;
        return false; // standard return value
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT)
            firingFlag = true;
        return false; // standard return value
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        try {
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = entities.get(i);
                if (ComponentMappers.identity.get(entity).getId() == shapeWarsModel.shipId) {
                    PositionComponent positionComponent = ComponentMappers.position.get(entity);
                    SpriteComponent spriteComponent = ComponentMappers.sprite.get(entity);

                    // todo unite with functionality in firing system?
                    float x = (float) (positionComponent.getPosition().x + (spriteComponent.getSprite().getWidth() / 2));
                    float y = (float) (positionComponent.getPosition().y + (spriteComponent.getSprite().getHeight() / 2));
                    Vector2 worldCoordinates = new Vector2(screenX, screenY);
                    fitViewport.unproject(worldCoordinates);

                    float deltaX = worldCoordinates.x - x;
                    float deltaY = worldCoordinates.y - y;
                    inputDirectionGun = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;
                    break;
                }
            }
        } catch (NullPointerException e) { }



        return false;
    }


    /*
     * todo: implement the shooting on mouse click and point the gun to the mouse pointer
     */
}
