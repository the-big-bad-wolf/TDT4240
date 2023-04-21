package com.mygdx.piratewars.model.systems;

import com.badlogic.ashley.core.Entity;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.model.helperSystems.FiringSystem;
import com.mygdx.piratewars.model.helperSystems.UpdateSystem;
import java.util.ArrayList;
import java.util.List;

public class UpdateSystemServer extends UpdateSystem {

    public List<Entity> unshotBullets;

    public UpdateSystemServer(PirateWarsModel pirateWarsModel) {
        super(pirateWarsModel);
        this.unshotBullets = new ArrayList<>();
    }

    public static UpdateSystem getInstance(PirateWarsModel pirateWarsModel) {
        if (instance == null) {
            synchronized (InputSystemDesktop.class) {
                if (instance == null) {
                    instance = new UpdateSystemServer(pirateWarsModel);
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