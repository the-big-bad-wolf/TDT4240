package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component{
  private int value;
  private int direction;

  public VelocityComponent(int value, int direction) {
    this.value = value;
    this.direction = direction;
  }

  public int getValue() {
    return value;
  }

  public int getDirection() {
    return direction;
  }

  public void setVelocity(int v, int d) {
    value = v;
    direction = d;
  }

  public void setValue(int v) {
    setVelocity(v, direction);
  }

  public void setDirection(int d) {
    setVelocity(value, d);
  }

  public void addDirection(int d) {
    direction += d;
  }
}
