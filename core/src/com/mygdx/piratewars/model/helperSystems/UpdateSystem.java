package com.mygdx.piratewars.model.helperSystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.network.data.BulletData;
import com.mygdx.piratewars.network.data.ShipData;

public abstract class UpdateSystem extends PirateWarsSystem {
    protected static volatile UpdateSystem instance;
    protected PirateWarsModel pirateWarsModel;
    public boolean updated; // works as a lock to avoid conflicts between threads

    public UpdateSystem(PirateWarsModel pirateWarsModel) {
        this.pirateWarsModel = pirateWarsModel;
    }

    public void dispose() {
        instance = null;
    }

    public void addedToEngine(Engine engine) { }

    public abstract void update(float deltaTime);

    public void addUnshotBullets(Entity entity) {
        return;
    }

    public void replaceData(ShipData[] shipsServer, BulletData[] bulletsServer, boolean isGameActive) {
        return;
    }
}