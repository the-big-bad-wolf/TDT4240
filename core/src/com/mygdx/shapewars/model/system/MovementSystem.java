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

	// todo remove everything sprite here, move to sprite system
	public void update(float deltaTime) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			PositionComponent position = ComponentMappers.position.get(entity);
			VelocityComponent velocity = ComponentMappers.velocity.get(entity);
			SpriteComponent sprite = ComponentMappers.sprite.get(entity);

			TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);
			float tileSize = collisionLayer.getTileWidth();

			// calculate old and new position values
			float radians = MathUtils.degreesToRadians * velocity.getDirection();

			float newX = position.getPosition().x + MathUtils.cos(radians) * velocity.getValue();
			float newY = position.getPosition().y + MathUtils.sin(radians) * velocity.getValue();

			// calculate the tank's bounding box
			Polygon tankBounds = new Polygon(new float[] {
					0, 0,
					sprite.getSprite().getWidth(), 0,
					sprite.getSprite().getWidth(), sprite.getSprite().getHeight(),
					0, sprite.getSprite().getHeight()
			});
			tankBounds.setOrigin(sprite.getSprite().getOriginX(), sprite.getSprite().getOriginY());
			tankBounds.setPosition(newX, newY);
			tankBounds.setRotation(velocity.getDirection());

			// check for collision with walls
			for (int x = 0; x < collisionLayer.getWidth(); x++) {
				for (int y = 0; y < collisionLayer.getHeight(); y++) {
					TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
					if (cell != null) {
						Rectangle rect = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
						Polygon tileBounds = new Polygon(new float[] {
								rect.x, rect.y, // bottom left corner
								rect.x + rect.width, rect.y, // bottom right corner
								rect.x + rect.width, rect.y + rect.height, // top right corner
								rect.x, rect.y + rect.height // top left corner
						});

						if (Intersector.overlapConvexPolygons(tankBounds, tileBounds)) {
							// tank collides with wall, adjust position
							Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
							Intersector.overlapConvexPolygons(tankBounds, tileBounds, mtv);
							Vector2 overlapVector = new Vector2(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
							newX += overlapVector.x;
							newY += overlapVector.y;
						}

					}
				}
			}

			// update position and rotation
			position.setPosition(newX, newY);
			sprite.getHitbox().setPosition(newX, newY);
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
