package com.mygdx.shapewars;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWars extends ApplicationAdapter {

	private ShapeWarsController controller;

	@Override
	public void create() {
		ShapeWarsModel model = new ShapeWarsModel();
		ShapeWarsView shapeWarsView = new ShapeWarsView(model); // todo make singleton and move to controller
		MainMenuView mainMenuView = new MainMenuView(model);
		controller = new ShapeWarsController(model, shapeWarsView, mainMenuView);
	}

	@Override
	public void render() {
		controller.update();
	}

	@Override
	public void dispose() {
		controller.dispose();
	}
}
