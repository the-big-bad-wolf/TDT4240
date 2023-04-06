package com.mygdx.shapewars;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.view.MainMenuView;

public class ShapeWars extends ApplicationAdapter {

	private Launcher launcher;
	private ShapeWarsController controller;

	public ShapeWars(Launcher launcher) {
		this.launcher = launcher;
	}

	@Override
	public void create() {
		controller = new ShapeWarsController(launcher);
		controller.setScreen(new MainMenuView(controller));

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
