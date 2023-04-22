package com.mygdx.piratewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.piratewars.controller.PirateWarsController;

public class ClientWaitingView implements Screen {
    private final Stage stage;
    private final UIBuilder uiBuilder;
    private PirateWarsController controller;
    private Sprite backgroundSprite;
    private TextButton exitButton;

    public ClientWaitingView(PirateWarsController controller) {
        this.controller = controller;
        this.stage = new Stage();
        this.uiBuilder = new UIBuilder(this.stage);

        Texture background = new Texture(Gdx.files.internal("images/background.png"));
        backgroundSprite = new Sprite(background);

        // make menu resizable
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        Gdx.input.setInputProcessor(stage);

        buildUI();
    }

    public void setController(PirateWarsController controller) {
        this.controller = controller;
    }

    @Override
    public void show() {
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
        backgroundSprite.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundSprite.setPosition((stage.getViewport().getWorldWidth() - backgroundSprite.getWidth()) / 2,
                (stage.getViewport().getWorldHeight() - backgroundSprite.getHeight()) / 2);
        backgroundSprite.draw(controller.gameModel.batch);
        controller.gameModel.batch.end();

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

    // todo rename variables and move to config
    private void buildUI() {
        float ipAddressWidth = 512;
        float ipAddressHeight = 128;
        float exitButtonWidth = 200;
        float exitButtonHeight = 100;
        float ipAddressXPos = Gdx.graphics.getWidth() / 2f - ipAddressWidth / 2;
        float ipAddressYPos = Gdx.graphics.getHeight() / 2f - ipAddressHeight / 2 + 100f;
        uiBuilder.buildButton("Waiting for host to start the game", ipAddressWidth, ipAddressHeight, ipAddressXPos, ipAddressYPos, "ipaddress");
        exitButton = uiBuilder.buildButton("Exit", exitButtonWidth, exitButtonHeight,
                ipAddressXPos, ipAddressYPos-300, "default");
        addActionsToUI();
    }

    public void addActionsToUI() {
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.pirateWarsModel.dispose();
                dispose();
                controller.setScreen(new MainMenuView(controller));
            }
        });
    }
}
