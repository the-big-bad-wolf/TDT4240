package com.mygdx.shapewars.model.systems;

import com.badlogic.ashley.core.Entity;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.helperSystems.FiringSystem;
import com.mygdx.shapewars.model.helperSystems.UpdateSystem;
import java.util.ArrayList;
import java.util.List;

public class UpdateSystemServer extends UpdateSystem {

    public List<Entity> unshotBullets;

    public UpdateSystemServer(ShapeWarsModel shapeWarsModel) {
        super(shapeWarsModel);
        this.unshotBullets = new ArrayList<>();
    }

    public static UpdateSystem getInstance(ShapeWarsModel shapeWarsModel) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new UpdateSystemServer(shapeWarsModel);
                }
            }
        }
        return instance;
    }

    @Override
    public void addUnshotBullets(Entity entity) {
        unshotBullets.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        // no iterator can be used here as the list gets accessed by the server thread
        for (int i = 0; i < unshotBullets.size(); i++) {
            FiringSystem.spawnBullet(unshotBullets.get(i));
        }
        unshotBullets.removeAll(unshotBullets);
    }
}