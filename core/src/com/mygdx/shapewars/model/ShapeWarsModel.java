package com.mygdx.shapewars.model;

import static com.mygdx.shapewars.config.GameConfig.ENEMY_FULL_HEALTH;
import static com.mygdx.shapewars.config.GameConfig.PLAYER_FULL_HEALTH;
import static com.mygdx.shapewars.config.GameConfig.SHIP_HEIGHT;
import static com.mygdx.shapewars.config.GameConfig.SHIP_WIDTH;

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
import com.mygdx.shapewars.model.system.UpdateSystemClient;
import com.mygdx.shapewars.model.system.UpdateSystemServer;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.network.server.ServerConnector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShapeWarsModel {
    public int numPlayers;
    public static Engine engine;
    private static TiledMap map;
    public Role role;
    public ServerConnector serverConnector; // todo implement strategy pattern
    public ClientConnector clientConnector;
    public HashMap<String, Integer> deviceShipMapping = new HashMap<>();
    public int shipId; // todo put this in a model just for clients
    public Joystick joystick;
    public GameModel gameModel;
    public boolean isGameActive;
    public ShapeWarsController controller;
    public boolean createEntitiesFlag;
    public UpdateSystemClient updateSystemClient;
    public UpdateSystemServer updateSystemServer;

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
            this.shipId = 0;
            this.deviceShipMapping = new HashMap<>();
            deviceShipMapping.put(this.gameModel.deviceId, shipId);
            this.serverConnector = new ServerConnector(this);
        } else {
            this.clientConnector = new ClientConnector(this, serverIpAddress);
        }

    }

    public void generateEntities() {
        if (this.role == Role.Server) {
            numPlayers = deviceShipMapping.size();
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
                Entity ship = new Entity();
                Vector2 cell = spawnCells.get(i);
                ship.add(new PositionComponent(cell.x * spawnLayer.getTileWidth(), cell.y * spawnLayer.getTileHeight()));
                ship.add(new VelocityComponent(0, 0));
                ship.add(new SpriteComponent(i == shipId ? PLAYER_FULL_HEALTH : ENEMY_FULL_HEALTH, SHIP_WIDTH, SHIP_HEIGHT)); // todo give own ship its own color
                ship.add(new HealthComponent(100));
                ship.add(new IdentityComponent(i));
                engine.addEntity(ship);
            }
            this.updateSystemServer = UpdateSystemServer.getInstance(this);
            engine.addSystem(updateSystemServer);
            isGameActive = true;
        } else {
            for (int i = 0; i < numPlayers; i++) {
                Entity ship = new Entity();
                ship.add(new PositionComponent(0, 0));
                ship.add(new VelocityComponent(0, 0));
                ship.add(new SpriteComponent(i == shipId ? PLAYER_FULL_HEALTH : ENEMY_FULL_HEALTH, SHIP_WIDTH, SHIP_HEIGHT)); // todo give own ship its own color
                ship.add(new HealthComponent(100));
                ship.add(new IdentityComponent(i));
                engine.addEntity(ship);
            }
            this.updateSystemClient = UpdateSystemClient.getInstance(this);
            engine.addSystem(updateSystemClient);
        }
        for (EntitySystem system : SystemFactory.generateSystems(this)) {
            engine.addSystem(system);
        }
    }

    public void update() {
        if (role == Role.Client) {
            if (!isGameActive) {
                // here send the keep alive requests
                clientConnector.sendLobbyRequest(gameModel.deviceId);
            }
            if (createEntitiesFlag) {
                createEntitiesFlag = false;
                generateEntities();
            }
        }
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
