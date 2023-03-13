package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
  private int health;

  public HealthComponent() {
    health = 100;
  }

  public int getHealth() {
    return health;
  }

  
  public void takeDamage(int damage) {
    health -= damage;
  }
}