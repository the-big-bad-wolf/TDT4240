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
    public static final int SHIP_WIDTH = 160;
    public static final int SHIP_HEIGHT = 120;
    public static final int MAX_BULLET_HEALTH = 3;
    public static final String PLAYER_FULL_HEALTH = "player_full_health.png";
    public static final String PLAYER_DAMAGE_ONE = "player_damage_one.png";
    public static final String PLAYER_DAMAGE_TWO = "player_damage_two.png";
    public static final String ENEMY_FULL_HEALTH = "enemy_full_health.png";
    public static final String ENEMY_DAMAGE_ONE = "enemy_damage_one.png";
    public static final String ENEMY_DAMAGE_TWO = "enemy_damage_two.png";
    public static final String CANNON_BALL = "cannon_ball.png";
    public static final Family TANK_FAMILY = Family.all(PositionComponent .class, VelocityComponent .class, SpriteComponent .class, HealthComponent .class, IdentityComponent.class).get();
}
