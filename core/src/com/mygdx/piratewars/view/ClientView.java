package com.mygdx.piratewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.piratewars.config.Role;
import com.mygdx.piratewars.controller.PirateWarsController;
import com.mygdx.piratewars.network.client.ClientConnector;

public class ClientView implements Screen {
    private final Stage stage;
    private final UIBuilder uiBuilder;
    private final PirateWarsController controller;
    private TextField inputField;
    private TextButton backButton;
    private TextButton okButton;
    private Sprite backgroundSprite;

    public ClientView(PirateWarsController controller) {
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

    private void buildUI() {
        float inputFieldWidth = 612;
        float inputFieldHeight = 128;
        float allButtonsWidth = 256f;
        float allButtonsHeight = 128f;
        float inputFieldXPos = Gdx.graphics.getWidth() / 2f - inputFieldWidth / 2;
        float inputFieldYPos = Gdx.graphics.getHeight() * 2f / 3f;
        float backButtonXPos = (Gdx.graphics.getWidth() - allButtonsWidth) / 2f  - 180f;
        float backButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 100f;
        float okButtonXPos = (Gdx.graphics.getWidth() - allButtonsWidth) / 2f + 180f;
        float okButtonYPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 100f;

        inputField = uiBuilder.buildTextField("Enter host IP address", "default", inputFieldWidth, inputFieldHeight, inputFieldXPos, inputFieldYPos);

        backButton = uiBuilder.buildButton("BACK", allButtonsWidth, allButtonsHeight,
                backButtonXPos, backButtonYPos, "default");
        okButton = uiBuilder.buildButton("OK", allButtonsWidth, allButtonsHeight, okButtonXPos, okButtonYPos, "default");

        addActionsToUI();
    }

    private void addActionsToUI() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                try {
                    controller.setScreen(new TutorialView(controller, 1));
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No controller found");
                }
            }
        });

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(inputField.getText());
                controller.generatePirateWarsModel(Role.Client, inputField.getText(), "");

                ClientConnector connector = (ClientConnector) controller.pirateWarsModel.connectorStrategy;

                if (!connector.client.isConnected()) {
                    stage.setKeyboardFocus(null);
                    inputField.setText("Enter valid IP address");
                    uiBuilder.buildButton("Invalid IP", 250, 128f,
                        Gdx.graphics.getWidth() / 2f - 250 / 2f,
                            Gdx.graphics.getHeight() * 2f / 3f - 150, "invalidIP");
                    controller.pirateWarsModel.dispose();
                }
                else {
                    controller.setScreen(new ClientWaitingView(controller));
                    dispose();
                }
            }
        });

        inputField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                // removes text once input is received
                if (focused) {
                    inputField.setText("");
                    inputField.setCursorPosition(0);
                }
            }
        });
    }
}