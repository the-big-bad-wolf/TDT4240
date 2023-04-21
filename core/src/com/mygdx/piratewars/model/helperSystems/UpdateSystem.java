package com.mygdx.shapewars.model.helperSystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.network.data.BulletData;
import com.mygdx.shapewars.network.data.ShipData;

public abstract class UpdateSystem extends PirateWarsSystem {
    protected static volatile UpdateSystem instance;
    protected ShapeWarsModel shapeWarsModel;
    public boolean updated; // works as a lock to avoid conflicts between threads

    public UpdateSystem(ShapeWarsModel shapeWarsModel) {
        this.shapeWarsModel = shapeWarsModel;
    }

    public void dispose() {
        instance = null;
    }

    public void addedToEngine(Engine engine) { }

    public abstract void update(float deltaTime);

    public void addUnshotBullets(Entity entity) {
        return;
    }

    public void replaceData(ShipData[] shipsServer, BulletData[] bulletsServer) {
        return;
    }
}