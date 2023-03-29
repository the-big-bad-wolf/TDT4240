package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component{
  private float value;
  private float direction;

  public VelocityComponent(float value, float direction) {
    this.value = value;
    this.direction = direction;
  }

  public float getValue() {
    return value;
  }

  public float getDirection() {
    return direction;
  }

  public void setVelocity(float v, float d) {
    value = v;
    direction += d;
  }

  public void setVelocityJoystick(float v, float d) {
    value = v;
    direction = d;
  }

  public void setValue(float v) {
    setVelocity(v, direction);
  }

  public void setDirection(float d) {
    setVelocity(value, d);
  }

  public void addDirection(float d) {
    direction += d;
  }
}
