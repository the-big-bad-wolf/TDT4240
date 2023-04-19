package com.mygdx.shapewars.model;

import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.config.Role;
import com.mygdx.shapewars.model.helperSystems.PirateWarsSystem;
import com.mygdx.shapewars.model.systems.DamageSystem;
import com.mygdx.shapewars.model.systems.DeathSystem;
import com.mygdx.shapewars.model.systems.InputSystemDesktop;
import com.mygdx.shapewars.model.systems.InputSystemMobile;
import com.mygdx.shapewars.model.systems.MovementSystem;
import com.mygdx.shapewars.model.systems.RicochetSystem;
import com.mygdx.shapewars.model.systems.SpriteSystem;

import java.util.ArrayList;
import java.util.List;

public class SystemFactory {
    public static List<PirateWarsSystem> generateSystems(ShapeWarsModel model) {
        Role role = model.role;
        Launcher launcher = model.gameModel.launcher;

        List<PirateWarsSystem> systems = new ArrayList<>();

        systems.add(SpriteSystem.getInstance(model));
        systems.add(launcher == Launcher.Desktop ?
                InputSystemDesktop.getInstance(model) : InputSystemMobile.getInstance(model));
        systems.add(DeathSystem.getInstance(model));

        if (role == Role.Server) {
            systems.add(MovementSystem.getInstance(model.shipObstacles));
            systems.add(RicochetSystem.getInstance(model.bulletObstacles));
            systems.add(DamageSystem.getInstance());
        }
        return systems;
    }
}
