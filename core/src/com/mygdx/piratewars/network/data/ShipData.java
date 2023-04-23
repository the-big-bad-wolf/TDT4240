package com.mygdx.piratewars.network.data;

import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;

public class ShipData {
    public VelocityComponent velocityComponent;
    public PositionComponent positionComponent;
    public HealthComponent healthComponent;
    public IdentityComponent identityComponent;

    public ShipData(VelocityComponent velocityComponent, PositionComponent positionComponent, HealthComponent healthComponent, IdentityComponent identityComponent) {
        this.velocityComponent = velocityComponent;
        this.positionComponent = positionComponent;
        this.healthComponent = healthComponent;
        this.identityComponent = identityComponent;
    }

    public ShipData() { }
}
