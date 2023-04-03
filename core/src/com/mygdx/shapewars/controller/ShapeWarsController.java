package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Game;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController extends Game {

	@Override
	public void create() {
		this.setScreen(new MainMenuView(this));
	}

	@Override
	public void render() {
		if (this.screen instanceof ShapeWarsView) {
			ShapeWarsModel.update();
		}
		super.render();
	}

	@Override
	public void dispose() {
	}
}
