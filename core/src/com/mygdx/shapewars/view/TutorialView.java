package com.mygdx.shapewars.view;

import java.net.UnknownHostException;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.controller.ShapeWarsController;

public class TutorialView implements Screen {
	private final ShapeWarsController controller;
	private final Stage stage;
	private final UIBuilder uiBuilder;
	private TextButton nextButton;
	private int sourceInt;
	private Sprite imageSprite;
	private Sprite backgroundSprite;
	private SpriteBatch batch;
	private ExtendViewport extendViewport;

	public TutorialView(ShapeWarsController controller, int sourceInt) {
		this.controller = controller;
		this.stage = new Stage();
		this.uiBuilder = new UIBuilder(this.stage);
		this.sourceInt = sourceInt;
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		batch = new SpriteBatch();
		extendViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		buildUI();
	}

	@Override
	public void show() {
		Texture background = new Texture(Gdx.files.internal("maps/mapExpansionGrass.png"));
		backgroundSprite = new Sprite(background);

		Texture image = new Texture(Gdx.files.internal("mainMenu/tutorial.png"));
		imageSprite = new Sprite(image);
		imageSprite.setScale(1f);
		imageSprite.setSize(Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight()-100);
		imageSprite.setPosition(7, (Gdx.graphics.getHeight()- imageSprite.getHeight())/2f);
		//imageSprite.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);

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
		backgroundSprite.setPosition((stage.getViewport().getWorldWidth() - backgroundSprite.getWidth())/2, (stage.getViewport().getWorldHeight()- backgroundSprite.getHeight()) / 2);
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

	private void buildUI() {
		float nextButtonsWidth = 180f;
		float nextButtonsHeight = 100f;
		float nextButtonXPos = Gdx.graphics.getWidth() - nextButtonsWidth;
		float nextButtonYPos = (Gdx.graphics.getHeight() - nextButtonsHeight)/2f;

		nextButton = uiBuilder.buildButton("Next", nextButtonsWidth, nextButtonsHeight,
				nextButtonXPos,
				nextButtonYPos,
				"redVersion");

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
}
