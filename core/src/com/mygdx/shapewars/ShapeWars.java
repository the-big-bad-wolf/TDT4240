package com.mygdx.shapewars;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.mygdx.shapewars.view.JoinView;

public class ShapeWars extends ApplicationAdapter {

	private Launcher launcher;
	private ShapeWarsController controller;

	public ShapeWars(Launcher launcher) {
		this.launcher = launcher;
	}

	@Override
	public void create() {
		ShapeWarsModel model = new ShapeWarsModel(launcher);
		ShapeWarsView shapeWarsView = new ShapeWarsView(model);
		MainMenuView mainMenuView = new MainMenuView(model);
		JoinView joinView = new JoinView(model);
		controller = new ShapeWarsController(model, shapeWarsView, mainMenuView, joinView);
		mainMenuView.setController(controller);
		shapeWarsView.setController(controller);
		joinView.setController(controller);
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
