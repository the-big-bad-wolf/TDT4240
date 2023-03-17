package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.shapewars.model.ShapeWarsModel;

public class MainMenuView implements Screen {
    private final float BUTTON_WIDTH = 750f;
    private final float BUTTON_HEIGHT = 200f;
    private final Stage stage;
    private final ShapeWarsModel model;

    public MainMenuView(ShapeWarsModel model) {
        this.model = model;
        this.stage = new Stage(); // todo check if we need to change that
        Gdx.input.setInputProcessor(stage);

        createButtons();
    }

    @Override
    public void show() {
        System.out.println("Main menu view showing");
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

    private void createButtons() {

        // Create button style
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = skin.getFont("default-font");
        buttonStyle.font.getData().setScale(3f);
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");

        // Create buttons on the MainMenu
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton hostButton = new TextButton("Host", skin);
        TextButton joinButton = new TextButton("Join", skin);

        // Set the size of the buttons
        startGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        hostButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        joinButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        // Set the position of the buttons
        startGameButton.setPosition(Gdx.graphics.getWidth() / 2 - startGameButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - startGameButton.getHeight() / 2 + 200);
        hostButton.setPosition(Gdx.graphics.getWidth() / 2 - hostButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - hostButton.getHeight() / 2);
        joinButton.setPosition(Gdx.graphics.getWidth() / 2 - joinButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - joinButton.getHeight() / 2 - 200);

        //Add spacings between the buttons
        hostButton.setPosition(hostButton.getX(), hostButton.getY() - 50f);
        joinButton.setPosition(joinButton.getX(), joinButton.getY() - 2 * 50f);

        // Add Clicklisteners to the buttons
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to ShapeWarsView
            }
        }); 

        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to HostView
            }
        }); 

        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to JoinView
            }
        });

        // Add the buttons to the stage
        stage.addActor(startGameButton);
        stage.addActor(hostButton);
        stage.addActor(joinButton);
    }
}
