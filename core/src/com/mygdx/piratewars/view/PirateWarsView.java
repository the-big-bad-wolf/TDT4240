package com.mygdx.piratewars.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.piratewars.config.Launcher;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.controller.PirateWarsController;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class PirateWarsView implements Screen {
    private OrthogonalTiledMapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer;
    private final Stage stage;
    private final PirateWarsModel model;
    private final TiledMap map;
    private FitViewport fitViewport;
    private Sprite backgroundSprite;
    private Sprite gameIsOverSprite;
    private ExtendViewport extendViewport;
    private PirateWarsController controller;
    private TextButton menuButton;
    private UIBuilder uiBuilder;

    public PirateWarsView(PirateWarsController controller) {
        this.controller = controller;
        this.model = controller.getPirateWarsModel();
        this.stage = new Stage(); // todo check if we need to change that
        map = PirateWarsModel.getMap();
        this.fitViewport = model.getPirateWarsViewport();
        this.uiBuilder = new UIBuilder(this.stage);
        stage.setViewport(fitViewport);

        int buttonHeight = 80;
        int buttonWidth = 150;
        menuButton = uiBuilder.buildButton("Menu", buttonWidth, buttonHeight, fitViewport.getWorldWidth()-buttonWidth, fitViewport.getWorldHeight()- buttonHeight, "redVersion");
        addActionsToUI();
    }

    public PirateWarsController getController() {
        return controller;
    }

    @Override
    public void show() {
        model.getMultiplexer().addProcessor(this.stage); // set stage as first input processor
        Gdx.input.setInputProcessor(model.getMultiplexer());
        // create a render object to easily render all layers, objects, etc. of our TileMap
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        shapeRenderer = new ShapeRenderer();

        // creation and setting of map to make sure dimensions are set right and whole map is shown
        float mapWidth = map.getProperties().get("width", Integer.class)
                * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class)
                * map.getProperties().get("tileheight", Integer.class);


        /* fitViewport scales the game world to fit on screen with the correct dimensions no extra
        * fitviewport anymore extendViewport allows for a scalable background that shows when
        * fitViewport doesn't use the whole screen */
        extendViewport = new ExtendViewport(mapWidth, mapHeight);

        // Background that shows around the actual playing field
        Texture background = new Texture(Gdx.files.internal("images/mapBackground.png"));
        backgroundSprite = new Sprite(background);

        Texture gameOver = new Texture(Gdx.files.internal("images/gameOver.png"));
        gameIsOverSprite = new Sprite(gameOver);

        render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fitViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true); // Add this line to update the stage's viewport


        int numLayers = map.getLayers().size();
        int[] layers = new int[numLayers];
        for (int i = 0; i < numLayers; i++) {
            layers[i] = i;
        }

        // drawing of the background, first sets the view to the extendViewport
        extendViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mapRenderer.setView((OrthographicCamera) extendViewport.getCamera());
        mapRenderer.getBatch().begin();
        backgroundSprite.setSize(extendViewport.getWorldWidth(), extendViewport.getWorldHeight());
        backgroundSprite.setPosition(-extendViewport.getWorldWidth() / 2, -extendViewport.getWorldHeight() / 2);
        backgroundSprite.draw(mapRenderer.getBatch());

        mapRenderer.getBatch().end();

        // drawing of actual map, therefore setting view back to fitViewpoint
        fitViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mapRenderer.setView((OrthographicCamera) fitViewport.getCamera());
        mapRenderer.render(layers);
        mapRenderer.getBatch().begin();
        for (int i = 0; i < model.getEngine().getEntities().size(); i++) {
            Entity entity = model.getEngine().getEntities().get(i);
            SpriteComponent spriteComponent = ComponentMappers.sprite.get(entity);
            spriteComponent.getSprite().draw(mapRenderer.getBatch());

            // render the aim helper if the player is alive
            if (ComponentMappers.identity.get(entity) != null 
                    && ComponentMappers.identity.get(entity).getId() == model.getShipId() 
                    && !ComponentMappers.health.get(entity).isDead()) {
                model.getAimHelp().draw(mapRenderer.getBatch());
            }
        }

        if (model.isGameActive() == false) {
            gameIsOverSprite.setSize(750, 250);
            gameIsOverSprite.setPosition((fitViewport.getWorldWidth() - 750) / 2f, (fitViewport.getWorldHeight() - 250) / 2f);
            gameIsOverSprite.draw(mapRenderer.getBatch());
        }


        mapRenderer.getBatch().end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (model.getGameModel().launcher == Launcher.Mobile) {
            // draw joystick
            shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(167 / 255f, 201 / 255f, 203 / 255f, 0.7f));
            shapeRenderer.circle(model.getJoystickShip().getOuterCircle().x, model.getJoystickShip().getOuterCircle().y, model.getJoystickShip().getOuterCircle().radius);
            shapeRenderer.circle(model.getJoystickGun().getOuterCircle().x, model.getJoystickGun().getOuterCircle().y, model.getJoystickGun().getOuterCircle().radius);
            shapeRenderer.setColor(new Color(123 / 255f, 147 / 255f, 149 / 255f, 0.4f));
            shapeRenderer.circle(model.getJoystickShip().getInnerCircle().x, model.getJoystickShip().getInnerCircle().y, model.getJoystickShip().getInnerCircle().radius);
            shapeRenderer.circle(model.getJoystickGun().getInnerCircle().x, model.getJoystickGun().getInnerCircle().y, model.getJoystickGun().getInnerCircle().radius);
            // draw fireButton
            shapeRenderer.circle(model.getFirebutton().getOuterCircle().x, model.getFirebutton().getOuterCircle().y, model.getFirebutton().getOuterCircle().radius);
        }
        shapeRenderer.end();


        stage.act(delta);
        stage.draw();
    }

    private void addActionsToUI() {
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    controller.setScreen(new MainMenuView(controller));
                    controller.getPirateWarsModel().dispose();
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No Controller found");
                }
            }
        });
    }

        @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
        extendViewport.update(width, height);
            stage.getViewport().update(width, height, true); // Add this line to update the stage's viewport
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
        map.dispose();
        mapRenderer.dispose();
    }
}
