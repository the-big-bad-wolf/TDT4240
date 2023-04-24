package com.mygdx.piratewars.controller;

import com.badlogic.gdx.Game;
import com.mygdx.piratewars.config.Launcher;
import com.mygdx.piratewars.config.Role;
import com.mygdx.piratewars.model.GameModel;
import com.mygdx.piratewars.model.PirateWarsModel;
import com.mygdx.piratewars.view.ClientWaitingView;
import com.mygdx.piratewars.view.MainMenuView;
import com.mygdx.piratewars.view.PirateWarsView;

public class PirateWarsController extends Game {

    private PirateWarsModel pirateWarsModel;
    private GameModel gameModel;
    private Launcher launcher;

    public PirateWarsController(Launcher launcher) {
        this.launcher = launcher;
    }

    public void create() {
        this.gameModel = new GameModel(launcher);
        this.setScreen(new MainMenuView(this));
    }

    public PirateWarsModel getPirateWarsModel() {
        return pirateWarsModel;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void render() {
        if (this.screen instanceof PirateWarsView || this.screen instanceof ClientWaitingView) {
            pirateWarsModel.update();
        }
        super.render();
    }

    public void generatePirateWarsModel(Role role, String serverIpAddress, String selectedMap) {
        this.pirateWarsModel = new PirateWarsModel(this, this.gameModel, role, serverIpAddress, selectedMap);
    }

    public void dispose() {
        gameModel.batch.dispose();
    }
}
