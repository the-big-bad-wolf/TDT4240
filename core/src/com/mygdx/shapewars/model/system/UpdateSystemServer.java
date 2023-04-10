package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.shapewars.model.ShapeWarsModel;

import java.util.ArrayList;
import java.util.List;

public class UpdateSystemServer extends EntitySystem {
    private static volatile UpdateSystemServer instance;
    protected ShapeWarsModel shapeWarsModel;

    public List<Entity> unshotBullets; // needed as a kind of "flag" (only for server side)

    public UpdateSystemServer(ShapeWarsModel shapeWarsModel) {
        this.unshotBullets = new ArrayList<>();
        this.shapeWarsModel = shapeWarsModel;
    }

    public static UpdateSystemServer getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new UpdateSystemServer(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    public void addedToEngine(Engine engine) { }

    public void update(float deltaTime) {
        // todo use while has next instead
        for (Entity e : unshotBullets) {
            FiringSystem.spawnBullet(e);
        }
        unshotBullets.removeAll(unshotBullets);
        // updated = false;
    }
}