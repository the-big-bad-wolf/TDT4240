package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component{
  private int speed;
  private int direction;

  public VelocityComponent(int speed, int direction) {
    this.speed = speed;
    this.direction = direction;
  }

  public int getSpeed() {
    return speed;
  }

  public int getDirection() {
    return direction;
  }

  public void setVelocity(int s, int d) {
    speed = s;
    direction = d;
  }

  public void setSpeed(int s) {
    setVelocity(s, direction);
  }

  public void setDirection(int d) {
    setVelocity(speed, d);
  }

  public void addDirection(int d) {
    direction += d;
  }
}
