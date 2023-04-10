package com.mygdx.shapewars.network.data;

import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class TankData {
    public VelocityComponent velocityComponent;
    public PositionComponent positionComponent;
    public HealthComponent healthComponent;
    public IdentityComponent identityComponent;

    public TankData(VelocityComponent velocityComponent, PositionComponent positionComponent, HealthComponent healthComponent, IdentityComponent identityComponent) {
        this.velocityComponent = velocityComponent;
        this.positionComponent = positionComponent;
        this.healthComponent = healthComponent;
        this.identityComponent = identityComponent;
    }

    public TankData() { }
}
