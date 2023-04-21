package com.mygdx.piratewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.piratewars.controller.PirateWarsController;

public class TutorialView implements Screen {
	private final PirateWarsController controller;
	private final Stage stage;
	private final UIBuilder uiBuilder;
	private TextButton nextButton;
	private TextButton backButton;
	private int sourceInt;
	private Sprite imageSprite;
	private Sprite backgroundSprite;
	private SpriteBatch batch;

	public TutorialView(PirateWarsController controller, int sourceInt) {
		this.controller = controller;
		this.stage = new Stage();
		this.uiBuilder = new UIBuilder(this.stage);
		this.sourceInt = sourceInt;
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		batch = new SpriteBatch();
		stage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		buildUI();
	}

	@Override
	public void show() {
		Texture background = new Texture(Gdx.files.internal("images/mapBackground.png"));
		backgroundSprite = new Sprite(background);

		Texture image = new Texture(Gdx.files.internal("images/tutorial.png"));
		imageSprite = new Sprite(image);
		imageSprite.setSize(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 100);
		imageSprite.setPosition(7, (Gdx.graphics.getHeight() - imageSprite.getHeight()) / 2f);

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
		backgroundSprite.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
		backgroundSprite.setPosition((stage.getViewport().getWorldWidth() - backgroundSprite.getWidth()) / 2,
				(stage.getViewport().getWorldHeight() - backgroundSprite.getHeight()) / 2);
		backgroundSprite.draw(batch);
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
		throw new UnsupportedOperationException("Unimplemented method 'pause'");
	}

	@Override
	public void resume() {
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

	private void buildUI() {
		float allButtonsWidth = 180f;
		float allButtonsHeight = 100f;
		float xPos = Gdx.graphics.getWidth() - allButtonsWidth;
		float nextButtonYPos = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - allButtonsHeight) / 2.5f;
		float backButtonYPos = (Gdx.graphics.getHeight() - allButtonsHeight) / 3f;


		nextButton = uiBuilder.buildButton("Next", allButtonsWidth, allButtonsHeight,
				xPos,
				nextButtonYPos,
				"redVersion");

		backButton = uiBuilder.buildButton("Back", allButtonsWidth, allButtonsHeight,
				xPos,
				backButtonYPos,
				"redVersion");

		addActionsToUI();
	}

	private void addActionsToUI() {
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				try {
					if (sourceInt == 0) {
						controller.setScreen(new SelectionView(controller, 0));
					}
					if (sourceInt == 1) {
						controller.setScreen(new ClientView(controller));
					}
					if (sourceInt == 2) {
						controller.setScreen(new SelectionView(controller, 2));
					}
				} catch (NullPointerException nullPointerException) {
					System.out.println("No controller found");
				}

			}
		});

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				try {
					if (sourceInt == 0) {
						controller.setScreen(new MainMenuView(controller));
					}
					if (sourceInt == 1) {
						controller.setScreen(new MainMenuView(controller));
					}
					if (sourceInt == 2) {
						controller.setScreen(new MainMenuView(controller));
					}
				} catch (NullPointerException nullPointerException) {
					System.out.println("No controller found");
				}

			}
		});
	}
}
