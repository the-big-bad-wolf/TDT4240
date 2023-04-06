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
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.controller.ShapeWarsController;

public class HostView implements Screen {
    private final Stage stage;
    private final ShapeWarsModel model;
    private final UIBuilder uiBuilder;
    private ShapeWarsController controller;
    private TextButton backButton;
    private TextButton startButton;
    private TextButton ipAddressField;
    private String ipAddress;



    public HostView(ShapeWarsModel model) {
        this.model = model;
        this.stage = new Stage();
        this.uiBuilder = new UIBuilder(this.stage);
        ipAddress = "IP Address";

        // make menu resizable
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        Gdx.input.setInputProcessor(stage);

        buildUI();
    }

    public void setController(ShapeWarsController controller) {
        this.controller = controller;
    }

    @Override
    public void show() {
        System.out.println("Join view showing");
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 1f, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getViewport().apply();

        model.batch.begin();
        model.batch.end();

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

    }

    private void buildUI() {
        float ipAddressWidth = 500;
        float ipAddressHeight = 100;
        float allButtonsWidth = 250f;
        float allButtonsHeight = 100f;
        float ipAddressXPos = Gdx.graphics.getWidth() / 2f - ipAddressWidth / 2;
        float ipAddressYPos = Gdx.graphics.getHeight() / 2f - ipAddressHeight / 2 + 100f;
        float backButtonXPos = Gdx.graphics.getWidth() / 2f - allButtonsWidth - 50f;
        float backButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 100f;
        float startButtonXPos = Gdx.graphics.getWidth() / 2f + 50f;
        float startButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 100f;

        ipAddressField = uiBuilder.buildButton(ipAddress, ipAddressWidth, ipAddressHeight, ipAddressXPos, ipAddressYPos);
        backButton = uiBuilder.buildButton("Back", allButtonsWidth, allButtonsHeight, backButtonXPos, backButtonYPos);
        startButton = uiBuilder.buildButton("Start", allButtonsWidth, allButtonsHeight, startButtonXPos, startButtonYPos);

        addActionsToUI();
    }

    private void addActionsToUI() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    controller.setScreen(controller.getMainMenuView());
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No controller found");
                }
            }
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Has to be decided and implemented
            }
        });
    }

}
