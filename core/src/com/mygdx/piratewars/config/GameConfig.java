package com.mygdx.piratewars.config;

import com.badlogic.ashley.core.Family;
import com.mygdx.piratewars.model.components.HealthComponent;
import com.mygdx.piratewars.model.components.IdentityComponent;
import com.mygdx.piratewars.model.components.ParentComponent;
import com.mygdx.piratewars.model.components.PositionComponent;
import com.mygdx.piratewars.model.components.SpriteComponent;
import com.mygdx.piratewars.model.components.VelocityComponent;

public abstract class GameConfig {
    public static final int MAX_SPEED = 5;
    public static final int MAX_TURN_RATE = 3;
    public static final int SHIP_WIDTH = 160;
    public static final int JOYSTICK_OUTER_CIRCLE_RADIUS = 120;
    public static final int JOYSTICK_INNER_CIRCLE_RADIUS = 50;
    public static final int SHIP_HEIGHT = 120;
    public static final int MAX_BULLET_HEALTH = 3;
    public static final int SHIP_OBSTACLE_LAYER = 5;
    public static final int BULLET_OBSTACLE_LAYER = 6;
    public static final String PLAYER_FULL_HEALTH = "player_full_health.png";
    public static final String PLAYER_DAMAGE_ONE = "player_damage_one.png";
    public static final String PLAYER_DAMAGE_TWO = "player_damage_two.png";
    public static final String ENEMY_FULL_HEALTH = "enemy_full_health.png";
    public static final String ENEMY_DAMAGE_ONE = "enemy_damage_one.png";
    public static final String ENEMY_DAMAGE_TWO = "enemy_damage_two.png";
    public static final String PLAYER_DEAD = "player_dead.png";
    public static final String ENEMY_DEAD = "enemy_dead.png";
    public static final String SHIP_DEAD = "ship_dead.png";
    public static final String CANNON_BALL = "cannon_ball.png";
    public static final Family SHIP_FAMILY = Family.all(PositionComponent .class, VelocityComponent .class, SpriteComponent .class, HealthComponent .class, IdentityComponent.class).get();
    public static final Family BULLET_FAMILY = Family.all(PositionComponent .class, VelocityComponent .class, SpriteComponent .class, HealthComponent .class, ParentComponent.class).exclude(IdentityComponent.class).get();
}
