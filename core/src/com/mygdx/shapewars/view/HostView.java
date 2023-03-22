package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.view.UIBuilder;

public class HostView implements Screen {
    private final Stage stage;
    private final ShapeWarsModel model;
    private final UIBuilder uiBuilder;
    private ShapeWarsController controller;
    private TextField inputField;
    private TextButton backButton;
    private TextButton okButton;

    public HostView(ShapeWarsModel model) {
        this.model = model;
        this.stage = new Stage();
        this.uiBuilder = new UIBuilder(this.stage);

        Gdx.input.setInputProcessor(stage);

        buildUI();
    }

    public void setController(ShapeWarsController controller) {
        this.controller = controller;
    }

    @Override
    public void show() {
        System.out.println("Host view showing");
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_STENCIL_BACK_VALUE_MASK);

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
        stage.dispose();
    }

    private void buildUI() {
        float inputFieldWidth = 500;
        float inputFieldHeight= 100;
        float allButtonsWidth = 250f;
        float allButtonsHeight = 100f;
        float inputFieldXPos = Gdx.graphics.getWidth() / 2 - inputFieldWidth / 2;
        float inputFieldYPos = Gdx.graphics.getHeight() / 2 + inputFieldHeight / 2 + 100f;
        float backButtonXPos = Gdx.graphics.getWidth() / 2 - allButtonsWidth - 50f;
        float backButtonYPos = Gdx.graphics.getHeight() / 2 - allButtonsHeight / 2 - 100f;
        float okButtonXPos = Gdx.graphics.getWidth() / 2 + 50f;
        float okButtonYPos = Gdx.graphics.getHeight() / 2 - allButtonsHeight / 2 - 100f;

        inputField = uiBuilder.buildTextField("String input", inputFieldWidth, inputFieldHeight, inputFieldXPos, inputFieldYPos);
        backButton = uiBuilder.buildButton("Back", allButtonsWidth, allButtonsHeight, backButtonXPos, backButtonYPos);
        okButton = uiBuilder.buildButton("OK", allButtonsWidth, allButtonsHeight, okButtonXPos, okButtonYPos);

        addActionsToUI();
    }

    private void addActionsToUI()
    {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to MainMenuView (has to be implemented after BugFix for HostButton)
            }
        }); 

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Has to be decided and implemented
            }
        });
    }
}