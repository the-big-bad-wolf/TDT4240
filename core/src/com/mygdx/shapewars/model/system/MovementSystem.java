package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class MovementSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;
	private TiledMap map;

  	private static volatile MovementSystem instance;

	private MovementSystem(TiledMap map) {
		this.map = map;
	}

	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, IdentityComponent.class).get());
	}

	public void update(float deltaTime) {
		for (Entity entity : entities) {
			PositionComponent position = ComponentMappers.position.get(entity);
			VelocityComponent velocity = ComponentMappers.velocity.get(entity);
			SpriteComponent sprite = ComponentMappers.sprite.get(entity);

			TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);
			float tileSize = collisionLayer.getTileWidth();

			// calculate old and new position values
			float radians = MathUtils.degreesToRadians * velocity.getDirection();

			float newX = position.getPosition().x + MathUtils.cos(radians) * velocity.getValue();
			float newY = position.getPosition().y + MathUtils.sin(radians) * velocity.getValue();


			// update position and rotation
			Vector2 overlapVector = CollisionSystem.getCollisionWithWall(entity, collisionLayer, newX, newY);
			newX += overlapVector.x;
			newY += overlapVector.y;
			position.setPosition(newX, newY);
			sprite.getSprite().setPosition(newX, newY);
			sprite.getHitbox().setPosition(newX, newY);
			sprite.setRotation(velocity.getDirection());
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
	public static MovementSystem getInstance(TiledMap map) {
		if (instance == null) {
			synchronized (MovementSystem.class) {
				if (instance == null) {
					instance = new MovementSystem(map);
				}
			}
		}
		return instance;
	}
}
