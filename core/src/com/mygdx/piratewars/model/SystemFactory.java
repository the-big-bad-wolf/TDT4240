package com.mygdx.piratewars.model;

import com.mygdx.piratewars.config.Launcher;
import com.mygdx.piratewars.config.Role;
import com.mygdx.piratewars.model.helperSystems.PirateWarsSystem;
import com.mygdx.piratewars.model.systems.DamageSystem;
import com.mygdx.piratewars.model.systems.DeathSystem;
import com.mygdx.piratewars.model.systems.InputSystemDesktop;
import com.mygdx.piratewars.model.systems.InputSystemMobile;
import com.mygdx.piratewars.model.systems.MovementSystem;
import com.mygdx.piratewars.model.systems.RicochetSystem;
import com.mygdx.piratewars.model.systems.SpriteSystem;

import java.util.ArrayList;
import java.util.List;

public class SystemFactory {
    public static List<PirateWarsSystem> generateSystems(PirateWarsModel model) {
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
