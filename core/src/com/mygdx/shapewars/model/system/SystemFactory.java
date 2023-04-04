package com.mygdx.shapewars.model.system;

import com.mygdx.shapewars.types.Launcher;
import com.mygdx.shapewars.types.Role;

public class SystemFactory {
    private Role role;
    private Launcher launcher;

    public SystemFactory(Role role, Launcher launcher) {
        this.role = role;
        this.launcher = launcher;
    }

    // this should return a list of all systems that need to be added to the engine according to the parameters
}
