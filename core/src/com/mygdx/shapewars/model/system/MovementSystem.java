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
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class).get());
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


			Rectangle wallsRect = checkCollisionWithWalls(newX, newY, sprite.getSprite().getWidth(), sprite.getSprite().getHeight(), (TiledMapTileLayer) map.getLayers().get(1));
			System.out.println(wallsRect);
			if (wallsRect != null) {
				// adjust newX and newY based on collision direction
				if (position.getPosition().x + sprite.getSprite().getWidth() <= wallsRect.getX()) {
					if (position.getPosition().y + sprite.getSprite().getHeight() <= wallsRect.getY()) {
						newY = wallsRect.getY() - sprite.getSprite().getHeight();
					} else if (position.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
						newY = wallsRect.getY() + wallsRect.getHeight();
					}
					// left collision
					newX = wallsRect.getX() - sprite.getSprite().getWidth();
				} else if (position.getPosition().x >= wallsRect.getX() + wallsRect.getWidth()) {
					if (position.getPosition().y + sprite.getSprite().getHeight() <= wallsRect.getY()) {
						newY = wallsRect.getY() - sprite.getSprite().getHeight();
					} else if (position.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
						newY = wallsRect.getY() + wallsRect.getHeight();
					}
					// right collision
					newX = wallsRect.getX() + wallsRect.getWidth();
				} else if (position.getPosition().y + sprite.getSprite().getHeight() <= wallsRect.getY()) {
					// top collision
					newY = wallsRect.getY() - sprite.getSprite().getHeight();
				} else if (position.getPosition().y >= wallsRect.getY() + wallsRect.getHeight()) {
					// bottom collision
					newY = wallsRect.getY() + wallsRect.getHeight();
				}
				// set new position
			}

			position.setPosition(newX, newY);

			// set direction
			sprite.getSprite().setRotation(velocity.getDirection());

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
