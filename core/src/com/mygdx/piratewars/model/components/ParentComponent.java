package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class ParentComponent implements Component {

    private Entity parent;

    public ParentComponent(Entity parent) {
        this.parent = parent;
    }

    public Entity getParent() {
        return this.parent;
    }
}
