package com.mygdx.piratewars.model.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component{
  private float value;
  private float direction;
  private float directionGun;

  public VelocityComponent(float value, float direction, float directionGun) {
    this.value = value;
    this.direction = direction;
    this.directionGun = directionGun;
  }

  public VelocityComponent(float value, float direction) {
    this(value, direction, direction);
  }

  public VelocityComponent() {} // needed for kryonet deserialization

  public float getValue() {
    return this.value;
  }

  public float getDirection() {
    return this.direction;
  }

  public float getDirectionGun() {
    return directionGun;
  }

  public void setVelocity(float v, float d, float directionGun) {
    this.value = v;
    this.direction = d;
    this.directionGun = directionGun;
  }

  public void setVelocity(float v, float d) {
    setVelocity(v, d, d);
  }

  public void setDirection(float directionShip, float directionGun) {
    this.setVelocity(value, directionShip, directionGun);
  }

  public void setDirection(float direction) {
    this.setVelocity(value, direction);
  }
}
