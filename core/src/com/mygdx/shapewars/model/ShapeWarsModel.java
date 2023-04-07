package com.mygdx.shapewars.model;

import static com.mygdx.shapewars.config.GameConfig.TANK_HEIGHT;
import static com.mygdx.shapewars.config.GameConfig.TANK_WIDTH;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.controller.ShapeWarsController;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.config.Role;
import com.mygdx.shapewars.model.system.SystemFactory;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.network.server.ServerConnector;
import com.mygdx.shapewars.view.ShapeWarsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShapeWarsModel {
    public int numPlayers = 2; // todo add lobby
    public static Engine engine;
    private static TiledMap map;
    public Role role;
    public ServerConnector serverConnector; // todo implement strategy pattern
    public ClientConnector clientConnector;
    public HashMap<String, Integer> clientTankMapping = new HashMap<>();
    public Joystick joystick;
    public GameModel gameModel;
    public boolean isGameActive;
    public ShapeWarsController controller;

    public ShapeWarsModel(ShapeWarsController controller, GameModel gameModel, Role role, String serverIpAddress) {
        this.role = role;
        this.gameModel = gameModel;
        this.controller = controller;

        TmxMapLoader loader = new TmxMapLoader();
        /*
            current map structure:
            0 = groundLayer
            1 = collisionLayer
            2 = bulletLayer
            3 = spawnLayer
            4, 5, ... = non-existent yet
         */
        map = loader.load("maps/map2.tmx"); // make server send this AFTER sophie is done
        engine = new Engine();
        joystick = new Joystick(400, 400, 300, 150);

        if (this.role == Role.Server) {
            // set number of players, map deviceIds to tankIds
            this.serverConnector = new ServerConnector(this);
            generateEntities();
        } else {
            this.clientConnector = new ClientConnector(this, serverIpAddress);
            // todo send initial request? (initial position <int, int>; map name <string>; tank sprite files <string[]>)
            // generateEntities(); // called when the game is active in the clientlistener
       }

        for (EntitySystem system : SystemFactory.generateSystems(this)) {
            engine.addSystem(system);
        }
    }

    public void generateEntities() {
        if (this.role == Role.Server) {
            TiledMapTileLayer spawnLayer = (TiledMapTileLayer) map.getLayers().get(3);

            List<Vector2> spawnCells = new ArrayList<>();
            for (int y = 0; y < spawnLayer.getHeight(); y++) {
                for (int x = 0; x < spawnLayer.getWidth(); x++) {
                    TiledMapTileLayer.Cell cell = spawnLayer.getCell(x, y);
                    if (cell != null) {
                        spawnCells.add(new Vector2(x, y));
                    }
                }
            }

            for (int i = 0; i < numPlayers; i++) {
                Entity tank = new Entity();
                Vector2 cell = spawnCells.get(i);
                tank.add(new PositionComponent(cell.x * spawnLayer.getTileWidth(), cell.y * spawnLayer.getTileHeight()));
                tank.add(new VelocityComponent(0, 0));
                tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT)); // change to support multiple colors
                tank.add(new HealthComponent(100));
                tank.add(new IdentityComponent(i));
                engine.addEntity(tank);
            }
        } else {
            for (int i = 0; i < numPlayers; i++) {
                Entity tank = new Entity();
                tank.add(new PositionComponent(0, 0));
                tank.add(new VelocityComponent(0, 0));
                tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT)); // todo change to support multiple colors
                tank.add(new HealthComponent(100));
                tank.add(new IdentityComponent(i));
                engine.addEntity(tank);
            }
        }
    }

    public static void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public static void addToEngine(Entity entity) {
      engine.addEntity(entity);
    }

    public static void removeFromEngine(Entity entity) {
        engine.removeEntity(entity);
      }

    public static TiledMap getMap() {
        return map;
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public static TiledMapTileLayer getLayer(int layerId) {
      return (TiledMapTileLayer) getMap().getLayers().get(layerId);
    }
}
