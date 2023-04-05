package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.model.ShapeWarsModel;

public class MainMenuView implements Screen {
    private final Stage stage;
    private final UIBuilder uiBuilder;
    private final ShapeWarsController controller;
    private TextButton startButton;
    private TextButton hostButton;
    private TextButton joinButton;

    public MainMenuView(ShapeWarsController controller) {
        this.controller = controller;
        this.stage = new Stage();
        this.uiBuilder = new UIBuilder(this.stage);

        Gdx.input.setInputProcessor(stage);

        buildUI();
    }

    @Override
    public void show() {
        // make menu resizable
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        System.out.println("Main menu view showing");
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 1f, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getViewport().apply();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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
        float allButtonsWidth = 750f;
        float allButtonsHeight = 200f;
        float startButtonXPos = Gdx.graphics.getWidth() / 2f - allButtonsWidth / 2;
        float startButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 + 200;
        float joinButtonXPos = Gdx.graphics.getWidth() / 2f - allButtonsWidth / 2;
        float joinButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 50f;
        float hostButtonXPos = Gdx.graphics.getWidth() / 2f - allButtonsWidth / 2;
        float hostButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 300;

        startButton = uiBuilder.buildButton("Start Game", allButtonsWidth, allButtonsHeight, startButtonXPos,
                startButtonYPos);
        joinButton = uiBuilder.buildButton("Join", allButtonsWidth, allButtonsHeight, hostButtonXPos, hostButtonYPos);
        hostButton = uiBuilder.buildButton("Host", allButtonsWidth, allButtonsHeight, joinButtonXPos, joinButtonYPos);

        addActionsToUI();
    }

    private void addActionsToUI() {
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                try {
                    controller.setScreen(new ShapeWarsView(new ShapeWarsModel(), controller));
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No Controller found");
                }
            }
        });

        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    controller.setScreen(new JoinView(controller));
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No Controller found");
                }
            }
        });

        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to JoinView (has to be decided and implemented)
            }
        });
    }
}
