package com.mygdx.shapewars.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShapeWarsModel {

    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;

    public float x, y;
    public int velocity;
    public float direction;
    public Sprite tankSprite;
    public SpriteBatch batch;

    public ShapeWarsModel() {
        batch = new SpriteBatch();
        tankSprite = new Sprite(new Texture("tank_graphics.png"));
        tankSprite.setSize(TANK_WIDTH, TANK_HEIGHT);
        tankSprite.setOrigin(tankSprite.getWidth() / 2f, tankSprite.getHeight() / 2f);

        // set starting position
        x = Gdx.graphics.getWidth() / 2f -  tankSprite.getWidth() / 2;
        y = Gdx.graphics.getHeight() / 2f -  tankSprite.getHeight() / 2;
    }
}
