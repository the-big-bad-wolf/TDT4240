package com.mygdx.shapewars;

import com.badlogic.gdx.Game;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.mygdx.shapewars.view.HostView;

public class ShapeWars extends Game {

	private ShapeWarsController controller;

	@Override
	public void create() {
		ShapeWarsModel model = new ShapeWarsModel();
		ShapeWarsView shapeWarsView = new ShapeWarsView(model);
		MainMenuView mainMenuView = new MainMenuView();
		HostView hostView = new HostView();
		controller = new ShapeWarsController(model, shapeWarsView, mainMenuView, hostView);
		mainMenuView.setController(controller);
		shapeWarsView.setController(controller);
		hostView.setController(controller);
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
