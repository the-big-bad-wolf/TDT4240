package com.mygdx.shapewars.network.server;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;

import java.io.IOException;

public class ServerConnector {

    private Server server;
    private Kryo kryo;
    private ShapeWarsModel model;

    public ServerConnector(ShapeWarsModel model) {
        this.server = new Server();
        this.server.start();
        this.kryo = kryo;

        try {
            server.bind(25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.kryo = server.getKryo();
        // todo remove unnecessary registrations
        this.kryo.register(InputRequest.class);
        this.kryo.register(GameResponse.class);
        this.kryo.register(ImmutableArray.class);
        this.kryo.register(Entity.class);
        this.kryo.register(Array.class);
        this.kryo.register(Object[].class);
        this.kryo.register(Object.class);
        this.kryo.register(Signal.class);
        this.kryo.register(PositionComponent.class);
        this.kryo.register(VelocityComponent.class);
        this.kryo.register(SpriteComponent.class);
        this.kryo.register(IdentityComponent.class);
        this.kryo.register(HealthComponent.class);
        this.kryo.register(Sprite.class);
        this.kryo.register(Polygon.class);
        this.kryo.register(Vector2.class);
        this.kryo.register(float[].class);
        this.kryo.register(Color.class);
        this.kryo.register(Rectangle.class);
        this.kryo.register(Texture.class);
        this.kryo.register(TextureRegion.class);
        this.kryo.register(FileTextureData.class);
        this.kryo.register(PositionComponent[].class);
        this.kryo.register(VelocityComponent[].class);

        this.server.addListener(new ServerListener(model));

    }
}
