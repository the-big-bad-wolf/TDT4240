package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
  private Vector2 position;

  public PositionComponent(float x, float y) {
    position = new Vector2(x, y);
  }

  public PositionComponent() {} // needed for kryonet deserialization

  public Vector2 getPosition() {
    return position;
  }

  
  public void setPosition(float x, float y) {
    position = new Vector2(x, y);
  }
}
