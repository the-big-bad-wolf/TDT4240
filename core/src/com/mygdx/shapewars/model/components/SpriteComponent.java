package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component{
  private Sprite sprite;

  public SpriteComponent(String filename, int width, int height) {
    sprite = new Sprite(new Texture(filename));
    sprite.setSize(width, height);
    sprite.setOrigin(width / 2, height / 2);
    sprite.setPosition(800, 800);
  }

  public Sprite getSprite() {
    return sprite;
  }
}
