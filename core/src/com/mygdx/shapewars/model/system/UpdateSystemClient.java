package com.mygdx.shapewars.model.system;

import static com.mygdx.shapewars.config.GameConfig.CANNON_BALL;
import static com.mygdx.shapewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.BulletData;
import com.mygdx.shapewars.network.data.ShipData;

public class UpdateSystemClient extends EntitySystem {
    private static volatile UpdateSystemClient instance;
    protected ImmutableArray<Entity> entities;
    protected ShapeWarsModel shapeWarsModel;
    public ShipData[] shipsServer;
    public BulletData[] bulletsServer;
    public boolean updated; // records if the system has updated all entities and can receive new data
    // works like a thread lock

    public UpdateSystemClient(ShapeWarsModel shapeWarsModel) {
        this.shapeWarsModel = shapeWarsModel;
    }

    public static UpdateSystemClient getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new UpdateSystemClient(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    public void addedToEngine(Engine engine) { }

    public void update(float deltaTime) {
        if (shipsServer == null || bulletsServer == null || !updated)
            return;

        // synchronize ship entities
        try {
            ImmutableArray<Entity> shipsClient = shapeWarsModel.engine.getEntitiesFor(SHIP_FAMILY);

            for (int i = 0; i < shipsClient.size(); i++) {
                boolean toRemove = true;
                Entity shipClient = shipsClient.get(i);
                for (int j = 0; j < shipsServer.length; j++) {
                    ShipData shipDataServer = shipsServer[j];
                    // i = index client side, j = index server side
                    if (shipDataServer.identityComponent.getId() == ComponentMappers.identity.get(shipClient).getId()) {
                        toRemove = false;

                        // update position
                        float x = shipDataServer.positionComponent.getPosition().x;
                        float y = shipDataServer.positionComponent.getPosition().y;
                        ComponentMappers.position.get(shipClient).setPosition(x, y);

                        // update velocity
                        float direction = shipDataServer.velocityComponent.getDirection();
                        float magnitude = shipDataServer.velocityComponent.getValue();
                        ComponentMappers.velocity.get(shipClient).setVelocity(magnitude, direction);

                        // update health
                        int health = shipDataServer.healthComponent.getHealth();
                        ComponentMappers.health.get(shipClient).setHealth(health);

                        break;
                    }
                }
                if (toRemove) {
                    // ship existing in client ecs does not exist on server ecs -> remove it
                    shapeWarsModel.engine.removeEntity(shipClient);
                }
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
            System.out.println(exception);
        }

        // synchronize bullet entities
        try {
            Family bulletFamily = Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class).exclude(IdentityComponent.class).get();
            ImmutableArray<Entity> bulletsClient = shapeWarsModel.engine.getEntitiesFor(bulletFamily);

            int numberOfBulletsServer = bulletsServer.length;
            int numberOfBulletsClient = bulletsClient.size();
            int diff = numberOfBulletsServer - numberOfBulletsClient;
            if (diff > 0) {
                // server has more bullets -> client needs to create #diff bullets
                for (int i = 0; i < diff; i++) {
                    Entity bullet = new Entity();
                    bullet.add(new PositionComponent(0, 0));
                    bullet.add(new VelocityComponent(0, 0));
                    bullet.add(new SpriteComponent(CANNON_BALL, 10, 10)); // todo why does a bullet have an image file??
                    bullet.add(new HealthComponent(3));
                    shapeWarsModel.engine.addEntity(bullet);
                }
            } else if (diff < 0) {
                // client has more entities -> client needs to delete #diff bullets
                diff = Math.abs(diff);
                for (int i = 0; i < diff; i++) {
                    Entity e = bulletsClient.get(numberOfBulletsClient - i - 1);
                    shapeWarsModel.engine.removeEntity(e); // not at once?
                }
            }

            bulletsClient = shapeWarsModel.engine.getEntitiesFor(bulletFamily);

            for (int i = 0; i < bulletsClient.size() && i < bulletsServer.length; i++) {
                Entity bulletClient = bulletsClient.get(i);
                BulletData bulletDataServer = bulletsServer[i];

                // update position
                float x = bulletDataServer.positionComponent.getPosition().x;
                float y = bulletDataServer.positionComponent.getPosition().y;
                ComponentMappers.position.get(bulletClient).setPosition(x, y);

                // update velocity
                float direction = bulletDataServer.velocityComponent.getDirection();
                float magnitude = bulletDataServer.velocityComponent.getValue();
                ComponentMappers.velocity.get(bulletClient).setVelocity(magnitude, direction);

                // update health
                int health = bulletDataServer.healthComponent.getHealth();
                ComponentMappers.health.get(bulletClient).setHealth(health);
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
            System.out.println(exception);
        } finally {
            updated = false;
        }
    }
}