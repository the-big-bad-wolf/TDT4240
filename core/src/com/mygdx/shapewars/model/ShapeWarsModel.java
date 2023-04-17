package com.mygdx.shapewars.model;

import static com.mygdx.shapewars.config.GameConfig.ENEMY_FULL_HEALTH;
import static com.mygdx.shapewars.config.GameConfig.PLAYER_FULL_HEALTH;
import static com.mygdx.shapewars.config.GameConfig.SHIP_HEIGHT;
import static com.mygdx.shapewars.config.GameConfig.SHIP_WIDTH;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.mygdx.shapewars.model.helperSystems.PirateWarsSystem;
import com.mygdx.shapewars.model.systems.UpdateSystemClient;
import com.mygdx.shapewars.model.systems.UpdateSystemServer;
import com.mygdx.shapewars.network.client.ClientConnector;
import com.mygdx.shapewars.network.server.ServerConnector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.shapewars.controller.Firebutton;
import java.io.IOException;
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
    public Joystick joystickShip;
    public Joystick joystickGun;
    public Firebutton firebutton;
    public ArrayList<Polygon> shipObstacles;
    public ArrayList<Polygon> bulletObstacles;
    public FitViewport shapeWarsViewport;
    public GameModel gameModel;
    public boolean isGameActive;
    public ShapeWarsController controller;
    public boolean createEntitiesFlag;
    public UpdateSystemClient updateSystemClient;
    public UpdateSystemServer updateSystemServer;
    public InputMultiplexer multiplexer;
    public Sprite aimHelp;
    public List<PirateWarsSystem> systems;

    public ShapeWarsModel(ShapeWarsController controller, GameModel gameModel, Role role, String serverIpAddress) {
        this.role = role;
        this.gameModel = gameModel;
        this.controller = controller;

        this.multiplexer = new InputMultiplexer();

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
        map = loader.load("maps/pirateMap.tmx"); // make server send this AFTER sophie is done
        engine = new Engine();

        OrthographicCamera camera = new OrthographicCamera();
        float mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);
        camera.setToOrtho(false, mapWidth, mapHeight);
        camera.update();
        // fitViewport scales the game world to fit on screen with the correct dimensions
        shapeWarsViewport = new FitViewport(mapWidth, mapHeight, camera);
        firebutton = new Firebutton(shapeWarsViewport.getWorldWidth()-180, 480, 120);
        joystickShip = new Joystick(180, 180, 120, 50);
        joystickGun = new Joystick((int) shapeWarsViewport.getWorldWidth()-180, 180, 120, 50);

        // TODO right layer
        shipObstacles = new ArrayList<Polygon>();
        // iterating over all map objects and adding them to ArrayList<Polygon> obstacles
        for (MapObject object : map.getLayers().get(5).getObjects()) {
            if (object instanceof PolygonMapObject) {
                Polygon rect = ((PolygonMapObject) object).getPolygon();
                shipObstacles.add(rect);
            }
        }

        // TODO right layer
        bulletObstacles = new ArrayList<Polygon>();

        // iterating over all map objects and adding them to ArrayList<Polygon> obstacles
        for (MapObject object : map.getLayers().get(6).getObjects()) {
            if (object instanceof PolygonMapObject) {
                Polygon rect = ((PolygonMapObject) object).getPolygon();
                bulletObstacles.add(rect);
            }
        }

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
                ship.add(new VelocityComponent(0, 0, 0));
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
                ship.add(new VelocityComponent(0, 0, 0));
                ship.add(new SpriteComponent(i == shipId ? PLAYER_FULL_HEALTH : ENEMY_FULL_HEALTH, SHIP_WIDTH, SHIP_HEIGHT)); // todo give own ship its own color
                ship.add(new HealthComponent(100));
                ship.add(new IdentityComponent(i));
                engine.addEntity(ship);
            }
            this.updateSystemClient = UpdateSystemClient.getInstance(this);
            engine.addSystem(updateSystemClient);
        }
        this.systems = SystemFactory.generateSystems(this);
        for (EntitySystem system : systems) {
            engine.addSystem(system);
        }

        this.aimHelp = new Sprite(new Texture("player_flag.png"));
        aimHelp.setSize(50, 20);
        aimHelp.setOrigin(-100, 0);
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

    public static TiledMap getMap() {
        return map;
    }

    public Joystick getJoystickShip() {
        return joystickShip;
    }

    public Joystick getJoystickGun() {
        return joystickGun;
    }


    public Firebutton getFirebutton() {
        return firebutton;
    }

    public void dispose() {
        for (PirateWarsSystem system : systems) {
            system.dispose();
        }
        engine.removeAllSystems();
        engine.removeAllEntities();
        try {
            if (role == Role.Server) {
                serverConnector.dispose();
            } else {
                clientConnector.dispose();
            }
        } catch (IOException e) {}
    }
}
