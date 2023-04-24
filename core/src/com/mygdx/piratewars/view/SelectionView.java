package com.mygdx.piratewars.view;

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
import com.mygdx.piratewars.config.Role;
import com.mygdx.piratewars.controller.PirateWarsController;

public class SelectionView implements Screen {
    private final PirateWarsController controller;
    private final Stage stage;
    private final UIBuilder uiBuilder;
    private TextButton map1Button;
    private TextButton map2Button;
    private TextButton backButton;
    private String map1;
    private String map2;
    private Sprite map1Sprite;
    private Sprite map2Sprite;
    private int sourceInt;
    private Sprite backgroundSprite;
    private SpriteBatch batch;

    public SelectionView(PirateWarsController controller, int sourceInt) {
        this.controller = controller;
        this.stage = new Stage();
        this.uiBuilder = new UIBuilder(this.stage);
        this.sourceInt = sourceInt;
        batch = new SpriteBatch();
        new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        buildUI();
        setMaps();
    }

    @Override
    public void show() {
        Texture background = new Texture(Gdx.files.internal("images/mapBackground.png"));
        backgroundSprite = new Sprite(background);

        Texture map1Texture = new Texture(Gdx.files.internal("images/Caribbean.png"));
        map1Sprite = new Sprite(map1Texture);

        Texture map2Texture = new Texture(Gdx.files.internal("images/MediterraneanSea.png"));
        map2Sprite = new Sprite(map2Texture);

        setMapSprites(960, 480);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        stage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 1f, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getViewport().apply();

        controller.getGameModel().batch.begin();
        controller.getGameModel().batch.end();

        batch.begin();
        backgroundSprite.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundSprite.setPosition((stage.getViewport().getWorldWidth() - backgroundSprite.getWidth()) / 2,
                (stage.getViewport().getWorldHeight() - backgroundSprite.getHeight()) / 2);
        backgroundSprite.draw(batch);
        setMaps();
        map1Sprite.draw(batch);
        map2Sprite.draw(batch);
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
        float allButtonsWidth = 600f;
        float allButtonsHeight = 100f;
        float selectWidth = 800;
        float selectHeight = 100;
        float backButtonWidth = 256f;

        float map1XPos = Gdx.graphics.getWidth() / 2f - (Gdx.graphics.getWidth() / 4f + allButtonsWidth / 2f);
        float map1YPos = Gdx.graphics.getHeight()/ 5f - allButtonsHeight;
        float map2XPos = Gdx.graphics.getWidth()/ 2f + (Gdx.graphics.getWidth() / 4f - allButtonsWidth / 2f);
        float map2YPos = Gdx.graphics.getHeight()/ 5f - allButtonsHeight;
        float selectXPos = Gdx.graphics.getWidth() / 2f - selectWidth / 2f;
        float selectYPos = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/6f + selectHeight / 2f);
        float backXPos = Gdx.graphics.getWidth() - backButtonWidth;
        float backYPos = Gdx.graphics.getHeight() - allButtonsHeight;

        map1Button = uiBuilder.buildButton("Caribbean", allButtonsWidth, allButtonsHeight,
                map1XPos,
                map1YPos,
                "default");

        map2Button = uiBuilder.buildButton("Mediterranean Sea", allButtonsWidth, allButtonsHeight,
                map2XPos,
                map2YPos,
                "default");

        backButton = uiBuilder.buildButton("Back", backButtonWidth, allButtonsHeight,
                backXPos,
                backYPos,
                "redVersion");

        uiBuilder.buildButton("Select your preferred map:",
                selectWidth,
                selectHeight,
                selectXPos,
                selectYPos,
                "default");


        addActionsToUI();
    }

    private void addActionsToUI() {
        map1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (sourceInt == 0) {
                        controller.generatePirateWarsModel(Role.Server, "", map1);
                        controller.setScreen(new HostView(controller));
                    }
                    if (sourceInt == 2) {
                        controller.generatePirateWarsModel(Role.Server, "", map1);
                        controller.getPirateWarsModel().generateEntities();
                        controller.setScreen(new PirateWarsView(controller));
                    }
                } catch (NullPointerException | UnknownHostException nullPointerException) {
                    System.out.println("No controller found");
                }
            }
        });

        map2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (sourceInt == 0) {
                        controller.generatePirateWarsModel(Role.Server, "", map2);
                        controller.setScreen(new HostView(controller));
                    }
                    if (sourceInt == 2) {
                        controller.generatePirateWarsModel(Role.Server, "", map2);
                        controller.getPirateWarsModel().generateEntities();
                        controller.setScreen(new PirateWarsView(controller));
                    }
                } catch (NullPointerException | UnknownHostException nullPointerException) {
                    System.out.println("No controller found");
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (sourceInt == 0) {
                        controller.setScreen(new TutorialView(controller, 0));
                    }
                    if (sourceInt == 2) {
                        controller.setScreen(new TutorialView(controller, 2));
                    }
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No controller found");
                }
            }
        });

    }

    private void setMaps() {
        this.map1 = "maps/Caribbean.tmx";
        this.map2 = "maps/MediterraneanSea.tmx";
    }

    private void setMapSprites(int width, int height) {
        map1Sprite.setSize(width, height);
        map2Sprite.setSize(width, height);


        float imagesYPos = Gdx.graphics.getHeight()/4f;
        float map1XPos = Gdx.graphics.getWidth() / 2f - (Gdx.graphics.getWidth() / 4f + width / 2f);
        float map2XPos = Gdx.graphics.getWidth()/ 2f + (Gdx.graphics.getWidth() / 4f - width / 2f);

        map1Sprite.setPosition(map1XPos, imagesYPos);
        map2Sprite.setPosition(map2XPos, imagesYPos);
    }
}