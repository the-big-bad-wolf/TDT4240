package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class RicochetSystem extends EntitySystem {
  private ImmutableArray<Entity> entities;

  private TiledMap map;

  private static volatile RicochetSystem instance;

  private RicochetSystem(TiledMap map) {
    this.map = map;
  };

  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class)
            .exclude(IdentityComponent.class).get());
  }

  public void update(float deltaTime) {
    for (Entity entity : entities) {
      PositionComponent position = ComponentMappers.position.get(entity);
      VelocityComponent velocity = ComponentMappers.velocity.get(entity);
      SpriteComponent sprite = ComponentMappers.sprite.get(entity);

      // calculate and set position
      float radians = MathUtils.degreesToRadians * velocity.getDirection();

      float newX = position.getPosition().x + MathUtils.cos(radians) * velocity.getValue();
      float newY = position.getPosition().y + MathUtils.sin(radians) * velocity.getValue();

      boolean hasHitX = false;
      boolean hasHitY = false;

      Rectangle wallsRect = checkCollisionWithWalls(newX, newY, sprite.getSprite().getWidth(),
          sprite.getSprite().getHeight(), (TiledMapTileLayer) map.getLayers().get(1));
      if (wallsRect != null) {
        // adjust newX and newY based on collision direction
        if (position.getPosition().x <= wallsRect.getX()) {
          System.out.println("RIGHT");
          hasHitX = true;
          if (position.getPosition().y + sprite.getSprite().getHeight() <= wallsRect.getY()) {
            System.out.println("ALSO TOP");
            newY = wallsRect.getY() - sprite.getSprite().getHeight();
            hasHitX = false;
            hasHitY = true;
          } else if (position.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
            System.out.println("AND BOTTOM");
            newY = wallsRect.getY() + wallsRect.getHeight();
          }
          newX = wallsRect.getX() - sprite.getSprite().getWidth();
          // left collision
        } else if (position.getPosition().x >= wallsRect.getX() + wallsRect.getWidth()) {
          System.out.println("LEFT");
          hasHitX = true;
          if (position.getPosition().y + sprite.getSprite().getHeight() <= wallsRect.getY()) {
            System.out.println("ACTUALLY TOP");
            newY = wallsRect.getY() - sprite.getSprite().getHeight();
            hasHitX = false;
            hasHitY = true;
            // right collision
          } else if (position.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
            System.out.println("ALSO BOTTOM");
            newY = wallsRect.getY() + wallsRect.getHeight();
            hasHitX = false;
            hasHitY = true;
          }
          newX = wallsRect.getX() + wallsRect.getWidth();
          // top collision
        } else if (position.getPosition().y + sprite.getSprite().getHeight() <= wallsRect.getY()) {
          System.out.println("TOP");
          newY = wallsRect.getY() - sprite.getSprite().getHeight();
          hasHitY = true;
          // bottom collision
        } else if (position.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
          System.out.println("BOTTOM");
          newY = wallsRect.getY() + wallsRect.getHeight();
          hasHitY = true;
        }
      }

      // set new position
      position.setPosition(newX, newY);

      // set direction
      if (hasHitX || hasHitY) {
        if (hasHitX && hasHitY) {
          velocity.setDirection((180 - velocity.getDirection()) * -1);
        } else if (hasHitX && !hasHitY) {
          velocity.setDirection(180 - velocity.getDirection());
        } else {
          velocity.setDirection(velocity.getDirection() * -1);
        }
        // System.out.println(velocity.getDirection());
      }

      sprite.getSprite().setRotation(velocity.getDirection() + 90);

      sprite.getSprite().setPosition(position.getPosition().x, position.getPosition().y);
    }
  }

  private Rectangle checkCollisionWithWalls(float x, float y, float width, float height, TiledMapTileLayer wallsLayer) {
    for (int col = 0; col < wallsLayer.getWidth(); col++) {
      for (int row = 0; row < wallsLayer.getHeight(); row++) {
        TiledMapTileLayer.Cell cell = wallsLayer.getCell(col, row);
        if (cell != null) {
          Rectangle rect = new Rectangle(col * wallsLayer.getTileWidth(), row * wallsLayer.getTileHeight(),
              wallsLayer.getTileWidth(), wallsLayer.getTileHeight());
          if (rect.overlaps(new Rectangle(x, y, width, height))) {
            return rect;
          }
        }
      }
    }
    return null;
  }

  public static RicochetSystem getInstance(TiledMap map) {
    if (instance == null) {
      synchronized (FiringSystem.class) {
        if (instance == null) {
          instance = new RicochetSystem(map);
        }
      }
    }
    return instance;
  }
}
