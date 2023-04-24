package com.mygdx.piratewars.model;

import static com.mygdx.piratewars.config.GameConfig.BULLET_OBSTACLE_LAYER;
import static com.mygdx.piratewars.config.GameConfig.ENEMY_FULL_HEALTH;
import static com.mygdx.piratewars.config.GameConfig.JOYSTICK_INNER_CIRCLE_RADIUS;
import static com.mygdx.piratewars.config.GameConfig.JOYSTICK_OUTER_CIRCLE_RADIUS;
import static com.mygdx.piratewars.config.GameConfig.PLAYER_FULL_HEALTH;
import static com.mygdx.piratewars.config.GameConfig.SHIP_HEIGHT;
import static com.mygdx.piratewars.config.GameConfig.SHIP_OBSTACLE_LAYER;
import static com.mygdx.piratewars.config.GameConfig.SHIP_WIDTH;

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
import com.mygdx.piratewars.config.Launcher;
import com.mygdx.piratewars.controller.PirateWarsController;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;
import com.mygdx.piratewars.config.Role;
import com.mygdx.piratewars.model.helperSystems.PirateWarsSystem;
import com.mygdx.piratewars.model.helperSystems.UpdateSystem;
import com.mygdx.piratewars.model.systems.UpdateSystemClient;
import com.mygdx.piratewars.model.systems.UpdateSystemServer;
import com.mygdx.piratewars.network.ConnectorStrategy;
import com.mygdx.piratewars.network.client.ClientConnector;
import com.mygdx.piratewars.network.server.ServerConnector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.piratewars.view.PirateWarsView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PirateWarsModel {
    public int shipId, numPlayers, numPlayersAlive;
    public boolean isGameActive, createEntitiesFlag, isWorldGenerated;
    public static Engine engine;
    private static TiledMap map;
    public Role role;
    public ConnectorStrategy connectorStrategy;
    public HashMap<String, Integer> deviceShipMapping;
    public Joystick joystickShip, joystickGun;
    public Firebutton firebutton;
    public ArrayList<Polygon> shipObstacles, bulletObstacles;
    public FitViewport pirateWarsViewport;
    public GameModel gameModel;
    public PirateWarsController controller;
    public UpdateSystem updateSystemStrategy;
    public String selectedMap;
    public InputMultiplexer multiplexer;
    public Sprite aimHelp;
    public List<PirateWarsSystem> systems;

    public PirateWarsModel(PirateWarsController controller, GameModel gameModel, Role role, String serverIpAddress,
            String selectedMap) {
        this.controller = controller;
        this.role = role;
        this.numPlayersAlive = -1;
        this.gameModel = gameModel;
        this.selectedMap = selectedMap;
        this.engine = new Engine();

        if (this.role == Role.Server) {
            this.shipId = 0;
            this.deviceShipMapping = new HashMap<>();
            deviceShipMapping.put(this.gameModel.deviceId, shipId);
            this.connectorStrategy = new ServerConnector(this);
            this.generateWorld(); // this happens in update() for the client as it needs to wait for the map
        } else {
            this.connectorStrategy = new ClientConnector(this, serverIpAddress);
        }
    }

    public void generateWorld() {
        this.multiplexer = new InputMultiplexer();

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load(selectedMap.isEmpty() ? "maps/pirateMap.tmx" : this.selectedMap);

        OrthographicCamera camera = new OrthographicCamera();
        float mapWidth = map.getProperties().get("width", Integer.class)
                * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class)
                * map.getProperties().get("tileheight", Integer.class);
        camera.setToOrtho(false, mapWidth, mapHeight);
        camera.update();
        pirateWarsViewport = new FitViewport(mapWidth, mapHeight, camera);

        if (gameModel.launcher == Launcher.Mobile) {
            firebutton = new Firebutton(pirateWarsViewport.getWorldWidth() - 180, 480, JOYSTICK_OUTER_CIRCLE_RADIUS);
            joystickShip = new Joystick(180, 180, JOYSTICK_OUTER_CIRCLE_RADIUS, JOYSTICK_INNER_CIRCLE_RADIUS);
            joystickGun = new Joystick((int) pirateWarsViewport.getWorldWidth() - 180, 180,
                    JOYSTICK_OUTER_CIRCLE_RADIUS,
                    JOYSTICK_INNER_CIRCLE_RADIUS);
        }

        shipObstacles = getLayerObstacles(SHIP_OBSTACLE_LAYER);
        bulletObstacles = getLayerObstacles(BULLET_OBSTACLE_LAYER);
    }

    public void generateEntities() {
        if (this.role == Role.Server) {
            numPlayers = deviceShipMapping.size();
            // todo maybe move this further up?
            this.updateSystemStrategy = UpdateSystemServer.getInstance(this);
            isGameActive = true;
            numPlayersAlive = numPlayers;
        } else {
            this.updateSystemStrategy = UpdateSystemClient.getInstance(this);
        }
        engine.addSystem(updateSystemStrategy);

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
            ship.add(
                    new SpriteComponent(i == shipId ? PLAYER_FULL_HEALTH : ENEMY_FULL_HEALTH, SHIP_WIDTH, SHIP_HEIGHT));
            ship.add(new HealthComponent(100));
            ship.add(new IdentityComponent(i));
            engine.addEntity(ship);
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
                connectorStrategy.sendLobbyRequest(gameModel.deviceId);
            }

            if (!this.selectedMap.isEmpty() && !this.isWorldGenerated) {
                System.out.println(this.selectedMap);
                generateWorld();
                isWorldGenerated = true;
            }

            if (createEntitiesFlag) {
                createEntitiesFlag = false;
                controller.getScreen().dispose();
                controller.setScreen(new PirateWarsView(controller));
                generateEntities();
            }
        }
        engine.update(Gdx.graphics.getDeltaTime());
        if (isGameActive && ((numPlayersAlive == 1 || numPlayersAlive == 0) && numPlayers >= 2)
                || (numPlayersAlive == 0 && numPlayers == 1)) {
            isGameActive = false;
        }
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

    public ArrayList<Polygon> getLayerObstacles(int layer) {
        ArrayList<Polygon> layerObstacles = new ArrayList<>();
        for (MapObject object : map.getLayers().get(layer).getObjects()) {
            if (object instanceof PolygonMapObject) {
                Polygon rect = ((PolygonMapObject) object).getPolygon();
                layerObstacles.add(rect);
            }
        }
        return layerObstacles;
    }

    public void dispose() {
        if (systems != null) {
            for (PirateWarsSystem system : systems) {
                system.dispose();
            }
        }
        try {
            engine.removeAllSystems();
            engine.removeAllEntities();
            connectorStrategy.dispose();
            updateSystemStrategy.dispose();
        } catch (Exception e) {
        }
    }
}
