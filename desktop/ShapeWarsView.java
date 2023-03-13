package com.mygdx.shapewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.shapewars.model.ShapeWarsModel;

import com.badlogic.gdx.maps.tiled.TiledMap;


public class ShapeWarsView implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    private final Stage stage;
    private final ShapeWarsModel model;

    public ShapeWarsView(ShapeWarsModel model) {
        this.model = model;
        this.stage = new Stage(); // todo check if we need to change that
    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/secondMap.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        System.out.println("Game view showing");
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int numLayers = map.getLayers().size();
        int[] layers = new int[numLayers];
        for (int i = 0; i < numLayers; i++) {
            layers[i] = i;
        }

        renderer.setView(camera);
        renderer.render(layers);

        renderer.getBatch().begin();
        model.tankSprite.draw(renderer.getBatch());
        renderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        map.dispose();
        renderer.dispose();
    }

    public TiledMapTileLayer getCollisionLayer() {
        return (TiledMapTileLayer) map.getLayers().get(1);
    }
}
