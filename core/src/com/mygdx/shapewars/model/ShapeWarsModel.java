package com.mygdx.shapewars.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.model.system.InputSystem;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.model.system.SpriteSystem;
import com.mygdx.shapewars.network.Role;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.network.server.ServerConnector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShapeWarsModel {
    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;
    public static final int NUM_PLAYERS = 2; // todo add lobby
    public SpriteBatch batch;
    public Engine engine;
    public MovementSystem movementSystem;
    private TiledMap map;
    private Role role = Role.Server; // todo change with screens
    public InputSystem inputSystem;
    public SpriteSystem spriteSystem;
    public ServerConnector serverConnector; // todo implement strategy pattern
    public ClientConnector clientConnector;
    public String clientId;
    public HashMap<String, Integer> clientTankMapping = new HashMap<>();

    public ShapeWarsModel() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/thirdMap.tmx"); // todo get from server
        batch = new SpriteBatch();
        engine = new Engine();

        if (this.role == Role.Server) {
            this.serverConnector = new ServerConnector(this);

            TiledMapTileLayer spawnLayer = (TiledMapTileLayer) map.getLayers().get(2);

            List<Vector2> spawnCells = new ArrayList<>();
            for (int y = 0; y < spawnLayer.getHeight(); y++) {
                for (int x = 0; x < spawnLayer.getWidth(); x++) {
                    TiledMapTileLayer.Cell cell = spawnLayer.getCell(x, y);
                    if (cell != null) {
                        spawnCells.add(new Vector2(x, y));
                    }
                }
            }

            for (int i = 0; i < NUM_PLAYERS; i++) {
                Entity tank = new Entity();
                Vector2 cell = spawnCells.get(i);
                tank.add(new PositionComponent(cell.x * spawnLayer.getTileWidth(), cell.y * spawnLayer.getTileHeight()));
                tank.add(new VelocityComponent(0, 0));
                tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT)); // todo change to support multiple colors
                tank.add(new HealthComponent());
                tank.add(new IdentityComponent(i));
                engine.addEntity(tank);
            }
            movementSystem = movementSystem.getInstance(map);
            engine.addSystem(movementSystem);


        } else if (this.role == Role.Client) {
            this.clientConnector = new ClientConnector(this);
            this.clientId = UUID.randomUUID().toString();
            // todo send initial request (initial position <int, int>; map name <string>; tank sprite files <string[]>)

            for (int i = 0; i < NUM_PLAYERS; i++) {
                Entity tank = new Entity();
                tank.add(new PositionComponent(0, 0));
                tank.add(new VelocityComponent(0, 0));
                tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT)); // todo change to support multiple colors
                tank.add(new HealthComponent());
                tank.add(new IdentityComponent(i));
                engine.addEntity(tank);
            }
        }

        inputSystem = inputSystem.getInstance(role, clientConnector, clientId);
        engine.addSystem(inputSystem);
        spriteSystem = spriteSystem.getInstance();
        engine.addSystem(spriteSystem);
    }

    public void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public TiledMap getMap() {
        return map;
    }
}
