package com.mygdx.shapewars.model;

import static com.mygdx.shapewars.config.GameConfig.ENEMY_TANK_SKIN;
import static com.mygdx.shapewars.config.GameConfig.FRIENDLY_TANK_SKIN;
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
import com.mygdx.shapewars.model.system.UpdateSystemClient;
import com.mygdx.shapewars.model.system.UpdateSystemServer;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.network.server.ServerConnector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.controller.Firebutton;
import com.mygdx.shapewars.config.Launcher;
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
    public HashMap<String, Integer> deviceTankMapping = new HashMap<>();
    public int tankId; // todo put this in a model just for clients
    public Joystick joystick;
    public Firebutton firebutton;
    public ArrayList<Polygon> obstacles;
    public FitViewport shapeWarsViewport;
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
            4 = collisionObjectLayer (defines the Polygons for collision detection)
            5, 6 ... = non-existent yet
         */
        map = loader.load("maps/mobileMap2.tmx"); // make server send this AFTER sophie is done
        engine = new Engine();
        joystick = new Joystick(400, 400, 300, 150);

        obstacles = new ArrayList<Polygon>();
        // iterating over all map objects and adding them to ArrayList<Polygon> obstacles
        for (MapObject object : map.getLayers().get(4).getObjects()) {
            if (object instanceof PolygonMapObject) {
                Polygon rect = ((PolygonMapObject) object).getPolygon();
                obstacles.add(rect);
            }
        }

        OrthographicCamera camera = new OrthographicCamera();
        float mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);
        camera.setToOrtho(false, mapWidth, mapHeight);
        camera.update();
        // fitViewport scales the game world to fit on screen with the correct dimensions
        shapeWarsViewport = new FitViewport(mapWidth, mapHeight, camera);
        joystick = new Joystick(100, 100, 100, 50);
        firebutton = new Firebutton(shapeWarsViewport.getWorldWidth()-100, 100, 50);

        if (this.role == Role.Server) {
            this.tankId = 0;
            this.deviceTankMapping = new HashMap<>();
            deviceTankMapping.put(this.gameModel.deviceId, tankId);
            this.serverConnector = new ServerConnector(this);
        } else {
            this.clientConnector = new ClientConnector(this, serverIpAddress);
        }

    }

    public void generateEntities() {
        if (this.role == Role.Server) {
            numPlayers = deviceTankMapping.size();
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
                tank.add(new SpriteComponent(i == tankId ? FRIENDLY_TANK_SKIN : ENEMY_TANK_SKIN, TANK_WIDTH, TANK_HEIGHT)); // todo give own tank its own color
                tank.add(new HealthComponent(100));
                tank.add(new IdentityComponent(i));
                engine.addEntity(tank);
            }
            this.updateSystemServer = UpdateSystemServer.getInstance(this);
            engine.addSystem(updateSystemServer);
            isGameActive = true;
        } else {
            for (int i = 0; i < numPlayers; i++) {
                Entity tank = new Entity();
                tank.add(new PositionComponent(0, 0));
                tank.add(new VelocityComponent(0, 0));
                tank.add(new SpriteComponent(i == tankId ? FRIENDLY_TANK_SKIN : ENEMY_TANK_SKIN, TANK_WIDTH, TANK_HEIGHT)); // todo give own tank its own color
                tank.add(new HealthComponent(100));
                tank.add(new IdentityComponent(i));
                engine.addEntity(tank);
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

    public Firebutton getFirebutton() {
        return firebutton;
    }
}
