package com.mygdx.shapewars.config;

import com.badlogic.ashley.core.Family;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public abstract class GameConfig {
    public static final int MAX_SPEED = 5;
    public static final int MAX_TURN_RATE = 3;
    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;
    public static final String FRIENDLY_TANK_SKIN = "tank_graphics.png";
    public static final String ENEMY_TANK_SKIN = "tank_graphics.png";
    public static final Family TANK_FAMILY = Family.all(PositionComponent .class, VelocityComponent .class, SpriteComponent .class, HealthComponent .class, IdentityComponent.class).get();
}
