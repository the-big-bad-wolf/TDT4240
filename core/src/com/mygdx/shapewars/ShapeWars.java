package com.mygdx.shapewars;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWars extends ApplicationAdapter {

	private ShapeWarsController controller;

	@Override
	public void create() {
		ShapeWarsModel model = new ShapeWarsModel();
		ShapeWarsView view = new ShapeWarsView(model);
		controller = new ShapeWarsController(model, view);
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
