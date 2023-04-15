package com.mygdx.shapewars.view;

import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.shapewars.controller.ShapeWarsController;

public class TutorialView implements Screen {
	private final ShapeWarsController controller;
	private final Stage stage;
	private final UIBuilder uiBuilder;
	private TextButton nextButton;
	private int sourceInt;
	private Sprite imageSprite;
	private SpriteBatch batch;

	public TutorialView(ShapeWarsController controller, int sourceInt) {
		this.controller = controller;
		this.stage = new Stage();
		this.uiBuilder = new UIBuilder(this.stage);
		this.sourceInt = sourceInt;
		batch = new SpriteBatch();
		buildUI();

	}

	private void buildUI() {
		float nextButtonsWidth = 250f;
		float nextButtonsHeight = 100f;
		float nextButtonXPos = Gdx.graphics.getWidth() / 2f - 250 / 2;
		float nextButtonYPos = 10;

		nextButton = uiBuilder.buildButton("Next", nextButtonsWidth, nextButtonsHeight,
				nextButtonXPos,
				nextButtonYPos);

		addActionsToUI();
	}

	private void addActionsToUI() {
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				try {
					if (sourceInt == 0) {
						controller.setScreen(new HostView(controller));
					}
					if (sourceInt == 1) {
						controller.setScreen(new ClientView(controller));
					}

				} catch (NullPointerException | UnknownHostException nullPointerException) {
					System.out.println("No controller found");
				}

			}
		});
	}

	@Override
	public void show() {
		Texture image = new Texture(Gdx.files.internal("mainMenu/tutorial.png"));
		imageSprite = new Sprite(image);
		imageSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 100);
		imageSprite.setY(100);
		Gdx.input.setInputProcessor(stage);
		render(0);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.6f, 0.8f, 1f, 0.8f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		stage.getViewport().apply();

		controller.gameModel.batch.begin();
		controller.gameModel.batch.end();

		batch.begin();
		imageSprite.draw(batch);
		batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'pause'");
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'resume'");
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
