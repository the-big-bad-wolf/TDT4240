package com.mygdx.piratewars.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;


public class SpriteComponent implements Component{
  private Sprite sprite;
  private Polygon hitbox;


  public SpriteComponent(String filename, int width, int height) {
    sprite = new Sprite(new Texture(filename));
    sprite.setSize(width, height);
    sprite.setOrigin(width / 2f, height / 2f);
    sprite.setPosition(800, 800);

    float[] vertices = {0, 0, width, 0, width, height, 0, height};
    hitbox = new Polygon(vertices);
    hitbox.setOrigin(width/2f, height/2f);
    hitbox.setPosition(sprite.getX(), sprite.getY());
  }

  public void setRotation(float angle) {
    sprite.setRotation(angle);
    hitbox.setRotation(angle);
  }

  public Sprite getSprite() {
    return sprite;
  }

  public Polygon getHitbox() {
    return hitbox;
  }
}
