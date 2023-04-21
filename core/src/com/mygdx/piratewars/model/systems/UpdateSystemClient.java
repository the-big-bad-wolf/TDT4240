package com.mygdx.piratewars.model.systems;

import static com.mygdx.piratewars.config.GameConfig.CANNON_BALL;
import static com.mygdx.piratewars.config.GameConfig.SHIP_FAMILY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.components.ComponentMappers;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;
import com.mygdx.piratewars.model.helperSystems.UpdateSystem;
import com.mygdx.piratewars.network.data.BulletData;
import com.mygdx.piratewars.network.data.ShipData;

public class UpdateSystemClient extends UpdateSystem {
    public ShipData[] shipsServer;
    public BulletData[] bulletsServer;

    public UpdateSystemClient(PirateWarsModel pirateWarsModel) {
        super(pirateWarsModel);
    }

    public static UpdateSystem getInstance(PirateWarsModel pirateWarsModel) {
        if (instance == null) {
            synchronized (UpdateSystemClient.class) {
                if (instance == null) {
                    instance = new UpdateSystemClient(pirateWarsModel);
                }
            }
        }
        return instance;
    }

    public void addedToEngine(Engine engine) { }

    public void replaceData(ShipData[] shipsServer, BulletData[] bulletsServer) {
        this.shipsServer = shipsServer;
        this.bulletsServer = bulletsServer;
    }

    public void update(float deltaTime) {
        if (shipsServer == null || bulletsServer == null || !updated)
            return;

        // synchronize ship entities
        try {
            ImmutableArray<Entity> shipsClient = pirateWarsModel.engine.getEntitiesFor(SHIP_FAMILY);
            for (int i = 0; i < shipsClient.size(); i++) {
                Entity shipClient = shipsClient.get(i);
                for (int j = 0; j < shipsServer.length; j++) {
                    ShipData shipDataServer = shipsServer[j];
                    // i = index client side, j = index server side
                    if (shipDataServer.identityComponent.getId() == ComponentMappers.identity.get(shipClient).getId()) {
                        // update position
                        float x = shipDataServer.positionComponent.getPosition().x;
                        float y = shipDataServer.positionComponent.getPosition().y;
                        ComponentMappers.position.get(shipClient).setPosition(x, y);

                        // update velocity
                        float directionShip = shipDataServer.velocityComponent.getDirection();
                        float directionGun = shipDataServer.velocityComponent.getDirectionGun();
                        float magnitude = shipDataServer.velocityComponent.getValue();
                        ComponentMappers.velocity.get(shipClient).setVelocity(magnitude, directionShip, directionGun);

                        // update health
                        int health = shipDataServer.healthComponent.getHealth();
                        ComponentMappers.health.get(shipClient).setHealth(health);

                        break;
                    }
                }
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception) {
            System.out.println(exception);
        }

        // synchronize bullet entities
        try {
            Family bulletFamily = Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class).exclude(IdentityComponent.class).get();
            ImmutableArray<Entity> bulletsClient = pirateWarsModel.engine.getEntitiesFor(bulletFamily);

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
                    pirateWarsModel.engine.addEntity(bullet);
                }
            } else if (diff < 0) {
                // client has more entities -> client needs to delete #diff bullets
                diff = Math.abs(diff);
                for (int i = 0; i < diff; i++) {
                    Entity e = bulletsClient.get(numberOfBulletsClient - i - 1);
                    pirateWarsModel.engine.removeEntity(e); // not at once?
                }
            }

            bulletsClient = pirateWarsModel.engine.getEntitiesFor(bulletFamily);

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