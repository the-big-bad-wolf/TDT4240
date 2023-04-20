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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.shapewars.config.Role;
import com.mygdx.shapewars.controller.ShapeWarsController;

public class SelectionView implements Screen {
    private final ShapeWarsController controller;
    private final Stage stage;
    private final UIBuilder uiBuilder;
    private TextButton map1Button;
    private TextButton map2Button;
    private String map1;
    private String map2;
    private int sourceInt;
    private Sprite imageSprite;
    private Sprite backgroundSprite;
    private SpriteBatch batch;
    private ExtendViewport extendViewport;

    public SelectionView(ShapeWarsController controller, int sourceInt) {
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
        setMaps();
    }

    @Override
    public void show() {
        Texture background = new Texture(Gdx.files.internal("maps/mapExpansionGrass.png"));
        backgroundSprite = new Sprite(background);

        Texture image = new Texture(Gdx.files.internal("mainMenu/tutorial.png"));
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
        float allButtonsWidth = 180f;
        float allButtonsHeight = 100f;

        float map1XPos = Gdx.graphics.getWidth() / 2f - allButtonsWidth - 50f;
        float map1YPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 100f;
        float map2XPos = Gdx.graphics.getWidth() / 2f + 50f;
        float map2YPos = Gdx.graphics.getHeight() / 2f - allButtonsHeight / 2 - 100f;

        map1Button = uiBuilder.buildButton("Map1", allButtonsWidth, allButtonsHeight,
                map1XPos,
                map1YPos,
                "redVersion");

        map2Button = uiBuilder.buildButton("Map2", allButtonsWidth, allButtonsHeight,
                map2XPos,
                map2YPos,
                "redVersion");

        addActionsToUI();
    }

    private void addActionsToUI() {
        map1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (sourceInt == 0) {
                        controller.generateShapeWarsModel(Role.Server, "", map1);
                        controller.setScreen(new HostView(controller));
                    }
                    if (sourceInt == 2) {
                        controller.generateShapeWarsModel(Role.Server, "", map1);
                        controller.shapeWarsModel.generateEntities();
                        controller.setScreen(new ShapeWarsView(controller));
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
                        controller.generateShapeWarsModel(Role.Server, "", map2);
                        controller.setScreen(new HostView(controller));
                    }
                    if (sourceInt == 2) {
                        controller.generateShapeWarsModel(Role.Server, "", map2);
                        controller.shapeWarsModel.generateEntities();
                        controller.setScreen(new ShapeWarsView(controller));
                    }
                } catch (NullPointerException | UnknownHostException nullPointerException) {
                    System.out.println("No controller found");
                }
            }
        });

    }

    private void setMaps() {
        this.map1 = "maps/pirateMap.tmx";
        this.map2 = "maps/pirateMap2.tmx";
    }
}