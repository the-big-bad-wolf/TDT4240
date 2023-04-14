package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.config.Role;
import com.mygdx.shapewars.model.ShapeWarsModel;
import java.util.ArrayList;
import java.util.List;

public class SystemFactory {
    public static List<EntitySystem> generateSystems(ShapeWarsModel model) {
        Role role = model.role;
        Launcher launcher = model.gameModel.launcher;

        List<EntitySystem> systems = new ArrayList<>();

        systems.add(SpriteSystem.getInstance());
        systems.add(launcher == Launcher.Desktop ?
                InputSystemDesktop.getInstance(model) : InputSystemMobile.getInstance(model));

        if (role == Role.Server) {
            systems.add(MovementSystem.getInstance(model.shipObstacles));
            systems.add(RicochetSystem.getInstance(model.bulletObstacles));
            systems.add(DeathSystem.getInstance());
        }
        return systems;
    }
}
