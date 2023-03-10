package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
  private Vector2 position;

  public PositionComponent(float x, float y) {
    position = new Vector2(x, y);
  }

  public Vector2 getPosition() {
    return position;
  }

  
  public void addPosition(float x, float y) {
    position = new Vector2(x, y);
  }
}
