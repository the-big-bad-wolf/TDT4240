package com.mygdx.shapewars.model.components;

import com.badlogic.ashley.core.Component;

public class IdentityComponent implements Component {

    private int id;

    public IdentityComponent(int id) {
        this.id = id;
    }

    public IdentityComponent() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
