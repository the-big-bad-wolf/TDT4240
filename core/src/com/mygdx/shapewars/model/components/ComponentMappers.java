package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.ComponentMapper;

public class ComponentMappers {
  public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
  public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
  public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
}
