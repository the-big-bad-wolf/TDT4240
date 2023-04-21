package com.mygdx.piratewars.model.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
  private int health;

  public HealthComponent(int health) {
    this.health = health;
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
}
