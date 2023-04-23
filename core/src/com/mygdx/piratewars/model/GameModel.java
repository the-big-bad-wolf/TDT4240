package com.mygdx.piratewars.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.piratewars.config.Launcher;
import java.util.UUID;

public class GameModel {
    public final String deviceId;
    public final Launcher launcher;
    public SpriteBatch batch;

    public GameModel(Launcher launcher) {
        this.launcher = launcher;
        this.deviceId = UUID.randomUUID().toString();
        this.batch = new SpriteBatch();
    }
}
