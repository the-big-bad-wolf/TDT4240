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

public class HostView implements Screen {
    private final float FIELD_WIDTH = 500f;
    private final float FIELD_HEIGHT = 100f;
    private final float BUTTON_WIDTH = 250f;
    private final float BUTTON_HEIGHT = 100f;
    private final Stage stage;
    private final ShapeWarsModel model;
    private TextField inputField;

    public HostView(ShapeWarsModel model) {
        this.model = model;
        this.stage = new Stage(); 
        Gdx.input.setInputProcessor(stage);

        createUI();
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

    private void createUI() {
        
        // Create textfield style
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        TextFieldStyle fieldStyle = new TextFieldStyle();
        fieldStyle.font = skin.getFont("default-font");
        fieldStyle.font.getData().setScale(3f);
        fieldStyle.background = skin.getDrawable("default-scroll");
        fieldStyle.cursor = skin.getDrawable("default-round");
        
        // Create button style
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = skin.getFont("default-font");
        buttonStyle.font.getData().setScale(3f);
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");

        // Create textfield
        inputField = new TextField("", fieldStyle);
        inputField.setMessageText("String input");

        // Create buttons
        TextButton backButton = new TextButton("Back", skin);
        TextButton okButton = new TextButton("OK", skin);

        // Set the size of the textfield
        inputField.setSize(FIELD_WIDTH, FIELD_HEIGHT);

        // Set the size of the buttons
        backButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        okButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        // Set the position of the textfield
        inputField.setPosition(Gdx.graphics.getWidth() / 2 - inputField.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 + inputField.getHeight() / 2 + 100f);

        // Set the position of the buttons
        backButton.setPosition(Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH - 50f,
                Gdx.graphics.getHeight() / 2 - BUTTON_HEIGHT / 2 - 100f);
        okButton.setPosition(Gdx.graphics.getWidth() / 2 + 50f,
                Gdx.graphics.getHeight() / 2 - BUTTON_HEIGHT / 2 - 100f);

        // Add Clicklisteners to the buttons
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to MainMenuView
            }
        }); 

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Do nothing
            }
        });

        // Add the buttons and textfield to the stage
        stage.addActor(inputField);
        stage.addActor(backButton);
        stage.addActor(okButton);
    }
}