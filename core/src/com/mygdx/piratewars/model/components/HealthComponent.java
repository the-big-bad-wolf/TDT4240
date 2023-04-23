package com.mygdx.piratewars.model.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
  private int health;
  private boolean dead;

  public HealthComponent(int health) {
    this.health = health;
    this.dead = false;
  }

  public HealthComponent() { } // needed for kryonet deserialization

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void takeDamage(int damage) {
    health -= damage;
  }

  public boolean isDead() {
    return this.dead;
  }

  public void setDead() {
    this.dead = true;
  }
}
