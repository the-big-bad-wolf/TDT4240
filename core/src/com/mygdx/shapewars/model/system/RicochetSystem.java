package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.badlogic.gdx.math.Polygon;
import java.util.ArrayList;

public class RicochetSystem extends PirateWarsSystem {
  private ImmutableArray<Entity> bullets;
  private ArrayList<Polygon> bulletObstacles;

  private static volatile RicochetSystem instance;

  private RicochetSystem(ArrayList<Polygon> bulletObstacles) {
    this.bulletObstacles = bulletObstacles;
  };

  public void addedToEngine(Engine engine) {
    bullets = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class)
            .exclude(IdentityComponent.class).get());
  }

  public void update(float deltaTime) {
    for (Entity bullet : bullets) {
      PositionComponent bulletPositionComponent = ComponentMappers.position.get(bullet);
      VelocityComponent bulletVelocityComponent = ComponentMappers.velocity.get(bullet);
      SpriteComponent bulletSpriteComponent = ComponentMappers.sprite.get(bullet);
      HealthComponent bulletHealthComponent = ComponentMappers.health.get(bullet);

      // calculate and set position
      float radians = MathUtils.degreesToRadians * bulletVelocityComponent.getDirection();

      float newX = bulletPositionComponent.getPosition().x
          + MathUtils.cos(radians) * bulletVelocityComponent.getValue() * deltaTime * 50;
      float newY = bulletPositionComponent.getPosition().y
          + MathUtils.sin(radians) * bulletVelocityComponent.getValue() * deltaTime * 50;

      boolean hasHitX = false;
      boolean hasHitY = false;

      Polygon rect = CollisionSystem.<Polygon>getCollisionWithWall(bullet, bulletObstacles, newX, newY);
      if (rect.getVertices().length > 0) {
        Rectangle wallsRect = rect.getBoundingRectangle();
        // adjust newX and newY based on collision direction
        if (bulletPositionComponent.getPosition().x <= wallsRect.getX()) {
          hasHitX = true;
          if (bulletPositionComponent.getPosition().y + bulletSpriteComponent.getSprite().getHeight() <= wallsRect
              .getY()) {
            newY = wallsRect.getY() - bulletSpriteComponent.getSprite().getHeight();
            hasHitX = false;
            hasHitY = true;
          } else if (bulletPositionComponent.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
            newY = wallsRect.getY() + wallsRect.getHeight();
          }
          newX = wallsRect.getX() - bulletSpriteComponent.getSprite().getWidth();
          // left collision
        } else if (bulletPositionComponent.getPosition().x >= wallsRect.getX() + wallsRect.getWidth()) {
          hasHitX = true;
          if (bulletPositionComponent.getPosition().y + bulletSpriteComponent.getSprite().getHeight() <= wallsRect
              .getY()) {
            newY = wallsRect.getY() - bulletSpriteComponent.getSprite().getHeight();
            hasHitX = false;
            hasHitY = true;
            // right collision
          } else if (bulletPositionComponent.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
            newY = wallsRect.getY() + wallsRect.getHeight();
            hasHitX = false;
            hasHitY = true;
          }
          newX = wallsRect.getX() + wallsRect.getWidth();
          // top collision
        } else if (bulletPositionComponent.getPosition().y + bulletSpriteComponent.getSprite().getHeight() <= wallsRect.getY()) {
          newY = wallsRect.getY() - bulletSpriteComponent.getSprite().getHeight();
          hasHitY = true;
          // bottom collision
        } else if (bulletPositionComponent.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
          newY = wallsRect.getY() + wallsRect.getHeight();
          hasHitY = true;
        }
      }


      // set new position
      bulletPositionComponent.setPosition(newX, newY);

      // set direction
      if (hasHitX || hasHitY) {
        bulletHealthComponent.takeDamage(1);
        if (hasHitX && hasHitY) {
          bulletVelocityComponent.setDirection((180 - bulletVelocityComponent.getDirection()) * -1);
        } else if (hasHitX && !hasHitY) {
          bulletVelocityComponent.setDirection(180 - bulletVelocityComponent.getDirection());
        } else {
          bulletVelocityComponent.setDirection(bulletVelocityComponent.getDirection() * -1);
        }
      }

      bulletSpriteComponent.getSprite().setRotation(bulletVelocityComponent.getDirection() + 90);

      bulletSpriteComponent.getSprite().setPosition(bulletPositionComponent.getPosition().x,
          bulletPositionComponent.getPosition().y);
    }
  }

  public static RicochetSystem getInstance(ArrayList<Polygon> obstacles) {
    if (instance == null) {
      synchronized (FiringSystem.class) {
        if (instance == null) {
          instance = new RicochetSystem(obstacles);
        }
      }
    }
    return instance;
  }

  public void dispose() {
    instance = null;
  }
}
