package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component{
  private float speed;
  private float direction;

  public VelocityComponent(int speed, int direction) {
    this.speed = speed;
    this.direction = direction;
  }

  public float getSpeed() {
    return speed;
  }

  public float getDirection() {
    return direction;
  }

  public void setVelocity(float s, float d) {
    speed = s;
    direction = d;
  }

  public void setSpeed(float s) {
    setVelocity(s, direction);
  }

  public void setDirection(float d) {
    setVelocity(speed, d);
  }

  public void addDirection(float d) {
    direction += d;
  }
}
