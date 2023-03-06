package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.shapewars.model.ShapeWarsModel;

public class ShapeWarsView {

    private final ShapeWarsModel model;

    public ShapeWarsView(ShapeWarsModel model) {
        this.model = model;
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        model.batch.begin();
        model.tankSprite.draw(model.batch);
        model.batch.end();
    }
}
