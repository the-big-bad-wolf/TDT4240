package com.mygdx.shapewars.network.data;

import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class BulletData {
    public VelocityComponent velocityComponent;
    public PositionComponent positionComponent;
    public HealthComponent healthComponent;

    public BulletData(VelocityComponent velocityComponent, PositionComponent positionComponent, HealthComponent healthComponent) {
        this.velocityComponent = velocityComponent;
        this.positionComponent = positionComponent;
        this.healthComponent = healthComponent;
    }

    public BulletData() { }
}
